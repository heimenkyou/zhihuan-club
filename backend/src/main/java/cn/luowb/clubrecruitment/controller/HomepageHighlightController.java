package cn.luowb.clubrecruitment.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.HomepageHighlightActivityReqDTO;
import cn.luowb.clubrecruitment.dto.req.HomepageHighlightOrderReqDTO;
import cn.luowb.clubrecruitment.dto.req.HomepageHighlightProjectReqDTO;
import cn.luowb.clubrecruitment.dto.resp.HomepageHighlightRespDTO;
import cn.luowb.clubrecruitment.service.HomepageHighlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "首页高光")
@RequestMapping("/api")
public class HomepageHighlightController {
    private final HomepageHighlightService homepageHighlightService;

    @Operation(summary = "查询首页高光列表")
    @GetMapping("/homepage/highlights")
    public Result<List<HomepageHighlightRespDTO>> list() {
        return Results.success(homepageHighlightService.listAll());
    }

    @Operation(summary = "后台查询首页高光列表")
    @SaCheckRole(value = {"normal", "super", "submitter"}, mode = SaMode.OR)
    @GetMapping("/admin/homepage-highlights")
    public Result<List<HomepageHighlightRespDTO>> adminList() {
        return Results.success(homepageHighlightService.listAll());
    }

    @Operation(summary = "添加项目高光")
    @SaCheckRole(value = {"normal", "super", "submitter"}, mode = SaMode.OR)
    @PostMapping("/admin/homepage-highlights/project")
    public Result<Void> addProject(@RequestBody @Valid HomepageHighlightProjectReqDTO reqDTO) {
        homepageHighlightService.addProject(reqDTO.getProjectId());
        return Results.success();
    }

    @Operation(summary = "添加活动高光")
    @SaCheckRole(value = {"normal", "super", "submitter"}, mode = SaMode.OR)
    @PostMapping("/admin/homepage-highlights/activity")
    public Result<Void> addActivity(@RequestBody @Valid HomepageHighlightActivityReqDTO reqDTO) {
        homepageHighlightService.addActivity(reqDTO);
        return Results.success();
    }

    @Operation(summary = "更新活动高光")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    @PutMapping("/admin/homepage-highlights/activity/{id}")
    public Result<Void> updateActivity(@PathVariable Long id,
                                       @RequestBody @Valid HomepageHighlightActivityReqDTO reqDTO) {
        homepageHighlightService.updateActivity(id, reqDTO);
        return Results.success();
    }

    @Operation(summary = "重排首页高光")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    @PutMapping("/admin/homepage-highlights/order")
    public Result<Void> reorder(@RequestBody @Valid HomepageHighlightOrderReqDTO reqDTO) {
        homepageHighlightService.reorder(reqDTO.getIds());
        return Results.success();
    }

    @Operation(summary = "删除首页高光")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    @DeleteMapping("/admin/homepage-highlights/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        homepageHighlightService.delete(id);
        return Results.success();
    }
}
