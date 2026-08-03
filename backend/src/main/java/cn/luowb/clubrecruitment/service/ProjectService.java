package cn.luowb.clubrecruitment.service;

import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.dao.entity.ProjectDO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.req.ProjectSaveReqDTO;
import cn.luowb.clubrecruitment.dto.resp.ProjectDetailRespDTO;
import cn.luowb.clubrecruitment.dto.resp.ProjectEditRespDTO;
import cn.luowb.clubrecruitment.dto.resp.ProjectRespDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;

/** 项目服务。 */
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

    /**
     * 添加或更新项目信息
     *
     * @param projectId 项目id
     * @param reqDTO    项目信息
     */
    Long saveOrUpdateProject(Long projectId, @Valid ProjectSaveReqDTO reqDTO);

    /**
     * 查询项目编辑回显信息
     *
     * @param projectId 项目id
     * @return 项目编辑回显信息
     */
    ProjectEditRespDTO getProjectEdit(Long projectId);

    /**
     * 删除项目
     *
     * @param projectId 项目id
     */
    void delete(Long projectId);
}
