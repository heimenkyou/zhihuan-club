package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.dao.entity.MajorMappingDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/** 专业映射服务。 */
public interface MajorMappingService extends IService<MajorMappingDO> {

    /**
     * 查询所有专业
     *
     * @return 专业列表
     */
    Map<String, MajorMappingDO> majors();
}
