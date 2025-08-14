package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.dao.entity.MessageDO;
import cn.luowb.clubrecruitment.dto.req.MessageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MessagePageRespDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 针对表【message(用户留言表)】的数据库操作Service
 */
public interface MessageService extends IService<MessageDO> {

    /**
     * 添加留言
     *
     * @param requestParam 请求参数
     */
    void createMessage(MessageReqDTO requestParam);

    /**
     * 分页获取留言列表
     *
     * @param requestParam 请求参数
     * @return 留言列表
     */
    IPage<MessagePageRespDTO> getMessageList(Page<MessageDO> requestParam);

    /**
     * 给留言点赞
     *
     * @param id 留言id
     */
    void likeMessage(Long id);
}
