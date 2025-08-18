package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.luowb.clubrecruitment.common.context.IPContext;
import cn.luowb.clubrecruitment.common.enums.LikeAction;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.exception.ServiceException;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.util.RedisKeyUtil;
import cn.luowb.clubrecruitment.dao.entity.MessageDO;
import cn.luowb.clubrecruitment.dao.mapper.MessageMapper;
import cn.luowb.clubrecruitment.dto.req.MessageReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MessagePageRespDTO;
import cn.luowb.clubrecruitment.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 针对表【message(用户留言表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageDO>
        implements MessageService {
    private final MessageMapper messageMapper;
    private final StringRedisTemplate redisTemplate;
    private final RedisKeyUtil redisKeyUtil;

    @Override
    public void createMessage(MessageReqDTO requestParam) {
        if (StringUtils.isBlank(requestParam.getNickname())) {
            throw new ClientException("昵称不能为空");
        }
        if (StringUtils.isBlank(requestParam.getContent())) {
            throw new ClientException("留言内容不能为空");
        }
        String ip = IPContext.getIp();
        MessageDO messageDO = new MessageDO()
                .setContent(requestParam.getContent())
                .setNickname(requestParam.getNickname())
                .setIpAddress(ip)
                .setLikeCount(0);
        if (!this.save(messageDO)) {
            throw new ServiceException("留言失败");
        }
    }

    @Override
    public PageData<MessagePageRespDTO> getMessageList(PageReqDTO requestParam) {
        String ip = IPContext.getIp();
        Page<MessageDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        // 时间倒序排序
        page = this.page(page, new LambdaQueryWrapper<MessageDO>().orderByDesc(MessageDO::getCreateTime));
        // 转换成返回参数
        return PageData.of(page, each -> {
            MessagePageRespDTO respDTO = BeanUtil.toBean(each, MessagePageRespDTO.class);
            respDTO.setLiked(this.hasLiked(each.getId())); // 判断当前用户是否已点赞
            respDTO.setCanDelete(each.getIpAddress().equals(ip)); // 判断当前用户是否可删除此留言
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
        String ip = IPContext.getIp();
        String redisKey = redisKeyUtil.buildMessageLikeKey(id, ip);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            // 已点赞 -> 取消
            messageMapper.unlikeMessage(id);
            redisTemplate.delete(redisKey);
            return LikeAction.UNLIKED;
        } else {
            // 未点赞 -> 点赞
            messageMapper.likeMessage(id);
            redisTemplate.opsForValue().set(redisKey, "1", redisKeyUtil.likeIntervalSeconds, TimeUnit.SECONDS);
            return LikeAction.LIKED;
        }
    }

    @Override
    public void deleteMessage(Long id) {
        MessageDO messageDO = this.getById(id);
        if (messageDO == null) {
            throw new ClientException("留言不存在");
        }
        String ip = IPContext.getIp();
        if (!messageDO.getIpAddress().equals(ip)) {
            throw new ClientException("只有留言对应的 IP 才能删除留言");
        }
        if (!this.removeById(id)) {
            throw new ServiceException("删除留言失败");
        }
        // todo 删除redis中的点赞记录
    }

    public boolean hasLiked(Long id) {
        String ip = IPContext.getIp();
        String redisKey = redisKeyUtil.buildMessageLikeKey(id, ip);
        return Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
    }
}




