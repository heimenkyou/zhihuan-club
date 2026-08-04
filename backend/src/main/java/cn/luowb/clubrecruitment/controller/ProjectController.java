package cn.luowb.clubrecruitment.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.req.ProjectSaveReqDTO;
import cn.luowb.clubrecruitment.dto.resp.ProjectDetailRespDTO;
import cn.luowb.clubrecruitment.dto.resp.ProjectEditRespDTO;
import cn.luowb.clubrecruitment.dto.resp.ProjectRespDTO;
import cn.luowb.clubrecruitment.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "项目")
@RequestMapping("/api")
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "分页查询项目")
    @GetMapping("/projects")
    public Result<PageData<ProjectRespDTO>> page(@ParameterObject PageReqDTO requestParam) {
        log.info("分页查询项目, 页码: {}, 每页数量: {}", requestParam.getCurrent(), requestParam.getSize());
        PageData<ProjectRespDTO> pageData = projectService.getPage(requestParam);
        return Results.success(pageData);
    }

    @Operation(summary = "后台分页查询项目")
    @SaCheckRole(value = {"normal", "super", "submitter"}, mode = SaMode.OR)
    @GetMapping("/admin/projects")
    public Result<PageData<ProjectRespDTO>> adminPage(@ParameterObject PageReqDTO requestParam) {
        return Results.success(projectService.getPage(requestParam));
    }

    @Operation(summary = "查询指定项目详情")
    @GetMapping("/projects/{projectId}")
    public Result<ProjectDetailRespDTO> getById(@PathVariable Long projectId) {
        log.info("查询指定项目详情, projectId: {}", projectId);
        ProjectDetailRespDTO projectDetailRespDTO = projectService.getProjectDetail(projectId);
        return Results.success(projectDetailRespDTO);
    }

    @PostMapping("/admin/projects")
    @Operation(summary = "创建新项目")
    @SaCheckRole(value = {"normal", "super", "submitter"}, mode = SaMode.OR)
    public Result<Long> createProject(@RequestBody @Valid ProjectSaveReqDTO reqDTO) {
        log.info("创建新项目, reqDTO={}", reqDTO);
        return Results.success(projectService.saveOrUpdateProject(null, reqDTO));
    }

    @PutMapping("/admin/projects/{id}")
    @Operation(summary = "更新项目")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    public Result<Void> updateProject(@PathVariable Long id,
                                      @RequestBody @Valid ProjectSaveReqDTO reqDTO) {
        log.info("更新项目, id={}, reqDTO={}", id, reqDTO);
        projectService.saveOrUpdateProject(id, reqDTO);
        return Results.success();
    }

    @GetMapping("/admin/projects/{projectId}")
    @Operation(summary = "项目信息编辑回显")
    @SaCheckRole(value = {"normal", "super", "submitter"}, mode = SaMode.OR)
    public Result<ProjectEditRespDTO> edit(@PathVariable Long projectId) {
        return Results.success(projectService.getProjectEdit(projectId));
    }

    @Operation(summary = "删除项目")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    @DeleteMapping("/admin/projects/{projectId}")
    public Result<Void> delete(@PathVariable Long projectId) {
        log.info("删除项目, projectId={}", projectId);
        projectService.delete(projectId);
        return Results.success();
    }
}
