package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.ProjectDO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.ProjectDetailRespDTO;
import cn.luowb.clubrecruitment.dto.resp.ProjectRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author heimenkyou
 * @description 针对表【project(项目基本信息表)】的数据库操作Service
 * @createDate 2025-08-27 17:39:11
 */
public interface ProjectService extends IService<ProjectDO> {

    /**
     * 分页查询项目
     *
     * @param requestParam 分页查询参数
     * @return 项目分页数据
     */
    PageData<ProjectRespDTO> getPage(PageReqDTO requestParam);

    /**
     * 查询指定项目详情
     *
     * @param projectId 项目id
     * @return 项目详情
     */
    ProjectDetailRespDTO getProjectDetail(Long projectId);
}
