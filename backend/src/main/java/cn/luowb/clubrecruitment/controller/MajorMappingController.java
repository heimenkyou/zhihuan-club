package cn.luowb.clubrecruitment.controller;

import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dao.entity.MajorMappingDO;
import cn.luowb.clubrecruitment.service.MajorMappingService;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.luowb.clubrecruitment.dto.resp.ApplicationPageDTO;
import cn.luowb.clubrecruitment.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "专业")
@RequestMapping("/api")
public class MajorMappingController {
    private final MajorMappingService majorMappingService;
    private final ApplicationService applicationService;

    @GetMapping("/majors")
    @Operation(summary = "获取专业代号与名称映射表")
    public Result<Map<String, MajorMappingDO>> list() {
        return Results.success(majorMappingService.majors());
    }

    @GetMapping("/admin/major-mappings")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    @Operation(summary = "获取后台专业映射列表")
    public Result<List<MajorMappingDO>> adminList() {
        return Results.success(majorMappingService.list());
    }

    @GetMapping("/admin/major-mappings/unmapped-applications")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    @Operation(summary = "获取未建立专业映射的报名记录")
    public Result<List<ApplicationPageDTO>> unmappedApplications() {
        return Results.success(applicationService.getUnmappedApplications());
    }

    @PostMapping("/admin/major-mappings")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    @Operation(summary = "新增专业映射")
    public Result<Void> create(@RequestBody MajorMappingDO mapping) {
        majorMappingService.createMapping(mapping);
        return Results.success();
    }

    @PutMapping("/admin/major-mappings/{code}")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    @Operation(summary = "更新专业映射")
    public Result<Void> update(@PathVariable String code, @RequestBody MajorMappingDO mapping) {
        majorMappingService.updateMapping(code, mapping);
        return Results.success();
    }
}
