package cn.luowb.clubrecruitment.dao.mapper;

import cn.luowb.clubrecruitment.dao.entity.ApplicationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/** 报名数据访问。 */
public interface ApplicationMapper extends BaseMapper<ApplicationDO> {

    /**
     * 查询所有不同的专业名称
     *
     * @return 专业名称列表
     */
    @Select("SELECT DISTINCT major FROM application WHERE TRIM(major) != '' ORDER BY major")
    List<String> selectDistinctMajors();

    /**
     * 查询所有不同的招新批次年份
     *
     * @return 年份列表（倒序）
     */
    @Select("SELECT DISTINCT application_year FROM application ORDER BY application_year DESC")
    List<Integer> selectDistinctYears();
}




