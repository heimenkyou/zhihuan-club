package cn.luowb.clubrecruitment.dao.mapper;

import cn.luowb.clubrecruitment.dao.entity.AwardDO;
import cn.luowb.clubrecruitment.dao.entity.ProjectAwardDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目奖项关联的数据访问接口。
 */
public interface ProjectAwardMapper extends BaseMapper<ProjectAwardDO> {
    /**
     * 按关联排序查询项目奖项。
     *
     * @param projectId 项目ID
     * @return 奖项列表
     */
    @Select("""
            SELECT a.*
            FROM award a
            INNER JOIN project_award pa ON pa.award_id = a.id
            WHERE pa.project_id = #{projectId}
            ORDER BY pa.sort_order ASC, pa.id ASC
            """)
    List<AwardDO> selectAwardsByProjectId(Long projectId);
}
