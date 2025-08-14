package cn.luowb.clubrecruitment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.luowb.clubrecruitment.common.context.IPContext;
import cn.luowb.clubrecruitment.common.exception.ClientException;
import cn.luowb.clubrecruitment.common.exception.ServiceException;
import cn.luowb.clubrecruitment.dao.entity.MessageDO;
import cn.luowb.clubrecruitment.dao.mapper.MessageMapper;
import cn.luowb.clubrecruitment.dto.req.MessageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MessagePageRespDTO;
import cn.luowb.clubrecruitment.service.MessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 针对表【message(用户留言表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageDO>
        implements MessageService {
    private final MessageMapper messageMapper;

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
    public IPage<MessagePageRespDTO> getMessageList(Page<MessageDO> requestParam) {
        Page<MessageDO> messageDOPage = messageMapper.selectPage(requestParam, null);
        // 转换成返回参数
        return messageDOPage.convert(each -> BeanUtil.toBean(each, MessagePageRespDTO.class));
    }

    @Override
    public void likeMessage(Long id) {
        // todo 短时间内使用ip防止刷赞
        // 查询是否有这个留言
        MessageDO messageDO = this.getById(id);
        if (messageDO == null) {
            throw new ClientException("留言不存在");
        }
        messageMapper.likeMessage(id);
    }
}




