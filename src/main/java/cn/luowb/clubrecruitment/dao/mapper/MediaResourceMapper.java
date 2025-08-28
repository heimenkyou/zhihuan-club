package cn.luowb.clubrecruitment.dao.mapper;

import cn.luowb.clubrecruitment.dao.entity.MediaResourceDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    /**
     * 查询未引用的媒体资源列表
     *
     * @return 未引用的媒体资源列表
     */
    @Select("select * from media_resource where ref_id is null or ref_id = 0 order by create_time desc")
    List<MediaResourceDO> selectUnreferenced();

    /**
     * 根据引用类型和ID清除引用
     *
     * @param refId   引用ID
     * @param refType 引用类型
     */
    @Update("update media_resource set ref_id = null, ref_type = null where ref_id = #{refId} and ref_type = #{refType}")
    void clearReferenceByRefId(Long refId, String refType);

    /**
     * 根据ID列表更新引用ID
     *
     * @param ids     媒体资源ID列表
     * @param refId   引用ID
     * @param refType 引用类型
     */
    void updateRefIdByIds(@Param("ids") List<Long> ids, @Param("refId") Long refId, @Param("refType") String refType);
}




