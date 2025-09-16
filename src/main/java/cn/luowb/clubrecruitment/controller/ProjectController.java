package cn.luowb.clubrecruitment.controller;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "项目")
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "分页查询项目")
    @GetMapping("/public/projects")
    public Result<PageData<ProjectRespDTO>> page(@ParameterObject PageReqDTO requestParam) {
        log.info("分页查询项目, 页码: {}, 每页数量: {}", requestParam.getCurrent(), requestParam.getSize());
        PageData<ProjectRespDTO> pageData = projectService.getPage(requestParam);
        return Results.success(pageData);
    }

    @Operation(summary = "查询指定项目详情")
    @GetMapping("/public/projects/{projectId}")
    public Result<ProjectDetailRespDTO> getById(@PathVariable Long projectId) {
        log.info("查询指定项目详情, projectId: {}", projectId);
        ProjectDetailRespDTO projectDetailRespDTO = projectService.getProjectDetail(projectId);
        return Results.success(projectDetailRespDTO);
    }

    @PostMapping("/admin/projects")
    @Operation(summary = "创建新项目")
    @Transactional
    public Result<Long> createProject(@RequestBody @Valid ProjectSaveReqDTO reqDTO) {
        return Results.success(projectService.saveProject(null, reqDTO));
    }

    @PutMapping("/admin/projects/{id}")
    @Operation(summary = "更新项目")
    @Transactional
    public Result<Void> updateProject(@PathVariable Long id,
                                      @RequestBody @Valid ProjectSaveReqDTO reqDTO) {
        projectService.saveProject(id, reqDTO);
        return Results.success();
    }

    @GetMapping("/projects/{projectId}/edit")
    @Operation(summary = "项目信息编辑回显")
    public Result<ProjectEditRespDTO> edit(@PathVariable Long projectId) {
        return Results.success(projectService.getProjectEdit(projectId));
    }
}
