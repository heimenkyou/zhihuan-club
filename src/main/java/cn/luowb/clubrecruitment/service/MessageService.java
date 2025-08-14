package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.MessageDO;
import cn.luowb.clubrecruitment.dto.req.MessagePageReqDTO;
import cn.luowb.clubrecruitment.dto.req.MessageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MessagePageRespDTO;
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
     * 给留言点赞
     *
     * @param id 留言id
     */
    void likeMessage(Long id);

    /**
     * 分页获取留言列表
     *
     * @param requestParam 请求参数
     * @return 留言列表
     */
    PageData<MessagePageRespDTO> getMessageList(MessagePageReqDTO requestParam);

    /**
     * 判断用户是否已经点赞
     *
     * @param id 留言id
     * @return true:已点赞 false:未点赞
     */
    boolean hasLiked(Long id);
}
