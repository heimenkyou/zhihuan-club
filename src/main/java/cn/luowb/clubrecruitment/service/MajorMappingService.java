package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.dao.entity.MajorMappingDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author heimenkyou
* @description 针对表【major_mapping(专业代号与名称映射表)】的数据库操作Service
* @createDate 2025-09-14 23:34:31
*/
public interface MajorMappingService extends IService<MajorMappingDO> {

    /**
     * 查询所有专业
     *
     * @return 专业列表
     */
    Map<String, MajorMappingDO> majors();

    /**
     * 查询专业简称
     *
     * @param studentId 学号
     * @return 专业简称
     */
    String getAbbreviation(String studentId);

    /**
     * 拼接班级姓名, 如物工B231张三
     * 通过学号查询报名表获取姓名
     *
     * @param studentId 学号
     * @return 班级姓名
     */
    String buildClassName(String studentId);

    /**
     * 拼接班级姓名, 如物工B231张三
     * 使用提供的姓名, 无需查询报名表
     *
     * @param studentId 学号
     * @param name 姓名
     * @return 班级姓名
     */
    String buildClassName(String studentId, String name);
}
