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
}
