package cn.luowb.clubrecruitment.dao.mapper;

import cn.luowb.clubrecruitment.dao.entity.ProjectDetailDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

/**
 * @author heimenkyou
 * @description 针对表【project_detail(项目详情表)】的数据库操作Mapper
 * @createDate 2025-08-27 17:40:22
 * @Entity cn.luowb.clubrecruitment.dao.entity.ProjectDetailDO
 */
public interface ProjectDetailMapper extends BaseMapper<ProjectDetailDO> {

    /**
     * 根据项目ID查询项目详情
     *
     * @param projectId 项目ID
     * @return 项目详情
     */
    @Select("select * from project_detail where project_id = #{projectId}")
    ProjectDetailDO selectByProjectId(Long projectId);

    /**
     * 根据项目ID删除项目详情
     *
     * @param projectId 项目ID
     */
    @Delete("delete from project_detail where project_id = #{projectId}")
    void deleteByProjectId(Long projectId);
}




