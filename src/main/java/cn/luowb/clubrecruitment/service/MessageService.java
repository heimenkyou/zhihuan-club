package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.common.enums.LikeAction;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.MessageDO;
import cn.luowb.clubrecruitment.dto.req.MessageReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
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
     * 分页获取留言列表
     *
     * @param requestParam 请求参数
     * @return 留言列表
     */
    PageData<MessagePageRespDTO> getMessageList(PageReqDTO requestParam);

    /**
     * 切换留言点赞状态
     *
     * @param id 留言id
     * @return 点赞状态
     */
    LikeAction toggleLikeMessage(Long id);

    /**
     * 删除留言
     *
     * @param id 留言id
     */
    void deleteMessage(Long id);
}
