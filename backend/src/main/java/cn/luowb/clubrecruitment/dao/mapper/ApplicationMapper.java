package cn.luowb.clubrecruitment.dao.mapper;

import cn.luowb.clubrecruitment.dao.entity.ApplicationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author heimenkyou
 * @description 针对表【application(用户报名表)】的数据库操作Mapper
 * @createDate 2025-08-18 23:29:05
 * @Entity cn.luowb.clubrecruitment.dao.entity.ApplicationDO
 */
public interface ApplicationMapper extends BaseMapper<ApplicationDO> {

    /**
     * 查询所有不同的专业名称
     *
     * @return 专业名称列表
     */
    @Select("SELECT DISTINCT major FROM application WHERE TRIM(major) != '' ORDER BY major")
    List<String> selectDistinctMajors();
}




