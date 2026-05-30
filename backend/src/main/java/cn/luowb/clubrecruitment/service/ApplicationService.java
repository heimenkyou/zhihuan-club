package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.ApplicationDO;
import cn.luowb.clubrecruitment.dto.req.ApplicationPageReqDTO;
import cn.luowb.clubrecruitment.dto.req.ApplicationReqDTO;
import cn.luowb.clubrecruitment.dto.resp.ApplicationPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author heimenkyou
 * @description 针对表【application(用户报名表)】的数据库操作Service
 * @createDate 2025-08-18 23:29:05
 */
public interface ApplicationService extends IService<ApplicationDO> {

    /**
     * 提交报名
     *
     * @param requestParam 报名参数
     */
    void createApplication(ApplicationReqDTO requestParam);

    /**
     * 分页查询报名信息
     *
     * @param requestParam 分页参数
     * @return 分页数据
     */
    PageData<ApplicationPageDTO> getApplicationList(ApplicationPageReqDTO requestParam);

    /**
     * 删除报名信息
     *
     * @param id 报名ID
     */
    void deleteApplication(Long id);

    /**
     * 获取报名信息中所有不重复的专业名称
     *
     * @return 专业名称列表
     */
    List<String> getApplicationMajors();
}
