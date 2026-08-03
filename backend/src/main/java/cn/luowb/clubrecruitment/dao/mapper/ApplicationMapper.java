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
}




