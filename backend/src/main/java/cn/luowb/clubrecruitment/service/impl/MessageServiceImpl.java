package cn.luowb.clubrecruitment.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.luowb.clubrecruitment.common.constant.RedisCacheKeyEnum;
import cn.luowb.clubrecruitment.common.context.IPContext;
import cn.luowb.clubrecruitment.common.context.VisitorTokenContext;
import cn.luowb.clubrecruitment.common.enums.LikeAction;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.exception.ServiceException;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.util.VisitorTokenUtil;
import cn.luowb.clubrecruitment.dao.entity.MessageDO;
import cn.luowb.clubrecruitment.dao.mapper.MessageMapper;
import cn.luowb.clubrecruitment.dto.req.MessageReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MessagePageRespDTO;
import cn.luowb.clubrecruitment.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/** 留言服务实现。 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageDO>
        implements MessageService {
    private final MessageMapper messageMapper;
    private final StringRedisTemplate redisTemplate;
    @Value("${app.visitor-token.secret:}")
    private String visitorTokenSecret;
    /** 点赞状态保存时长（天）。 */
    @Value("${app.like-expire-days:30}")
    private long likeExpireDays;

    @Override
    public void createMessage(MessageReqDTO requestParam) {
        String visitorToken = VisitorTokenContext.getToken();
        MessageDO messageDO = new MessageDO()
                .setContent(requestParam.getContent())
                .setNickname(requestParam.getNickname())
                .setAuthorTokenHash(VisitorTokenUtil.hashToken(visitorToken, visitorTokenSecret))
                .setIpAddress(IPContext.getIp())
                .setLikeCount(0);
        if (!this.save(messageDO)) {
            throw new ServiceException("留言失败");
        }
    }

    @Override
    public PageData<MessagePageRespDTO> getMessageList(PageReqDTO requestParam) {
        String visitorToken = VisitorTokenContext.getToken();
        // 检查是否是管理员（普通管理员或超级管理员），管理员可以删除所有留言
        boolean isAdmin = StpUtil.isLogin();

        Page<MessageDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        // 时间倒序排序
        page = this.page(page, new LambdaQueryWrapper<MessageDO>().orderByDesc(MessageDO::getCreateTime));
        // 批量判断当前访客对本页留言的点赞状态
        Set<Long> likedIds = getLikedIds(page.getRecords(), visitorToken);
        byte[] currentHash = VisitorTokenUtil.hashToken(visitorToken, visitorTokenSecret);
        // 转换成返回参数
        return PageData.of(page, each -> {
            MessagePageRespDTO respDTO = BeanUtil.toBean(each, MessagePageRespDTO.class);
            respDTO.setLiked(likedIds.contains(each.getId()));
            // 管理员可以删除所有留言，普通用户只能删除自己令牌对应的留言
            respDTO.setCanDelete(isAdmin || isOwnMessage(each.getAuthorTokenHash(), currentHash));
            return respDTO;
        });
    }

    @Override
    public LikeAction toggleLikeMessage(Long id) {
        // 查询是否有这个留言
        MessageDO messageDO = this.getById(id);
        if (messageDO == null) {
            throw new ClientException("留言不存在");
        }
        String visitorToken = VisitorTokenContext.getToken();
        String redisKey = RedisCacheKeyEnum.MESSAGE_LIKE_KEY.getKey(id, visitorToken);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            // 已点赞 -> 取消
            messageMapper.unlikeMessage(id);
            redisTemplate.delete(redisKey);
            return LikeAction.UNLIKED;
        } else {
            // 未点赞 -> 点赞，状态保存一个月
            messageMapper.likeMessage(id);
            redisTemplate.opsForValue().set(redisKey, "1", likeExpireDays, TimeUnit.DAYS);
            return LikeAction.LIKED;
        }
    }

    @Override
    public void deleteMessage(Long id) {
        MessageDO messageDO = this.getById(id);
        if (messageDO == null) {
            throw new ClientException("留言不存在");
        }

        // 检查是否是管理员（普通管理员或超级管理员），管理员可以直接删除
        boolean isAdmin = StpUtil.isLogin();

        if (!isAdmin) {
            // 非管理员需要校验留言作者令牌
            String visitorToken = VisitorTokenContext.getToken();
            byte[] currentHash = VisitorTokenUtil.hashToken(visitorToken, visitorTokenSecret);
            if (!isOwnMessage(messageDO.getAuthorTokenHash(), currentHash)) {
                throw new ClientException("只有留言对应的访客才能删除留言");
            }
        }

        if (!this.removeById(id)) {
            throw new ServiceException("删除留言失败");
        }
        // todo 删除redis中的点赞记录
    }

    /**
     * 批量查询当前访客对指定留言的点赞状态。
     *
     * @param records      留言记录
     * @param visitorToken 当前访客令牌
     * @return 已点赞的留言 ID 集合
     */
    private Set<Long> getLikedIds(List<MessageDO> records, String visitorToken) {
        Set<Long> likedIds = new HashSet<>();
        if (records == null || records.isEmpty()) {
            return likedIds;
        }
        List<String> keys = records.stream()
                .map(each -> RedisCacheKeyEnum.MESSAGE_LIKE_KEY.getKey(each.getId(), visitorToken))
                .toList();
        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) != null) {
                    likedIds.add(records.get(i).getId());
                }
            }
        }
        return likedIds;
    }

    /**
     * 判断留言作者令牌摘要是否与当前访客一致。
     *
     * @param storedHash  留言中保存的令牌摘要
     * @param currentHash 当前访客令牌摘要
     * @return 是否属于当前访客
     */
    private boolean isOwnMessage(byte[] storedHash, byte[] currentHash) {
        return storedHash != null && Arrays.equals(storedHash, currentHash);
    }
}
