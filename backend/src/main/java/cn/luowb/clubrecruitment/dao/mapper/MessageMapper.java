package cn.luowb.clubrecruitment.dao.mapper;

import cn.luowb.clubrecruitment.dao.entity.MessageDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/** 留言数据访问。 */
public interface MessageMapper extends BaseMapper<MessageDO> {

    /**
     * 点赞
     *
     * @param id 留言ID
     */
    @Update("update message set like_count = like_count + 1 where id = #{id}")
    void likeMessage(Long id);

    /**
     * 取消点赞
     *
     * @param id 留言ID
     */
    @Update("update message set like_count = like_count - 1 where id = #{id}")
    void unlikeMessage(Long id);
}




