package cn.luowb.clubrecruitment.dao.mapper;

import cn.luowb.clubrecruitment.dao.entity.MediaResourceDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author heimenkyou
 * @description 针对表【media_resource(媒体资源表)】的数据库操作Mapper
 * @createDate 2025-08-28 10:43:49
 * @Entity cn.luowb.clubrecruitment.dao.entity.MediaResourceDO
 */
public interface MediaResourceMapper extends BaseMapper<MediaResourceDO> {

    /**
     * 根据引用ID查询媒体资源列表
     *
     * @param refId 引用ID
     * @return 媒体资源列表
     */
    @Select("select * from media_resource where ref_id = #{refId}")
    List<MediaResourceDO> selectListByRefId(Long refId);
}




