package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.luowb.clubrecruitment.common.context.IPContext;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.exception.ServiceException;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.util.RedisKeyUtil;
import cn.luowb.clubrecruitment.dao.entity.MessageDO;
import cn.luowb.clubrecruitment.dao.mapper.MessageMapper;
import cn.luowb.clubrecruitment.dto.req.MessagePageReqDTO;
import cn.luowb.clubrecruitment.dto.req.MessageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MessagePageRespDTO;
import cn.luowb.clubrecruitment.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    public PageData<MessagePageRespDTO> getMessageList(MessagePageReqDTO requestParam) {
        Page<MessageDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        Page<MessageDO> messageDOPage = messageMapper.selectPage(page,
                new LambdaQueryWrapper<MessageDO>().orderByDesc(MessageDO::getCreateTime)); // 时间倒序排序
        // 转换成返回参数
        return PageData.of(messageDOPage, each -> BeanUtil.toBean(each, MessagePageRespDTO.class));
    }

    @Override
    public void likeMessage(Long id) {
        // 查询是否有这个留言
        MessageDO messageDO = this.getById(id);
        if (messageDO == null) {
            throw new ClientException("留言不存在");
        }
        String ip = IPContext.getIp();
        String redisKey = redisKeyUtil.buildMessageLikeKey(id, ip);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            throw new ClientException("不可重复点赞");
        }
        messageMapper.likeMessage(id);
        redisTemplate.opsForValue().set(redisKey, "1", RedisKeyUtil.LIKE_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public boolean hasLiked(Long id) {
        String ip = IPContext.getIp();
        String redisKey = redisKeyUtil.buildMessageLikeKey(id, ip);
        return Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
    }
}




