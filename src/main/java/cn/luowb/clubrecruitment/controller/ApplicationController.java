package cn.luowb.clubrecruitment.controller;

import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.ApplicationPageReqDTO;
import cn.luowb.clubrecruitment.dto.req.ApplicationReqDTO;
import cn.luowb.clubrecruitment.dto.resp.ApplicationPageDTO;
import cn.luowb.clubrecruitment.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "报名")
public class ApplicationController {
    private final ApplicationService applicationService;

    @Operation(summary = "提交报名")
    @PostMapping
    public Result<Void> createApplication(@RequestBody ApplicationReqDTO requestParam) {
        applicationService.createApplication(requestParam);
        return Results.success();
    }

    @Operation(summary = "分页查询报名信息")
    @GetMapping
    public Result<PageData<ApplicationPageDTO>> getApplicationList(@ParameterObject ApplicationPageReqDTO requestParam) {
        log.debug("分页查询报名信息, 第{}页{}条", requestParam.getCurrent(), requestParam.getSize());
        PageData<ApplicationPageDTO> applicationList = applicationService.getApplicationList(requestParam);
        return Results.success(applicationList);
    }

    @Operation(summary = "删除报名信息")
    @DeleteMapping("/{id}")
    public Result<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return Results.success();
    }

    @Operation(summary = "获取报名信息中所有不重复的专业名称")
    @GetMapping("/majors")
    public Result<List<String>> getApplicationMajors() {
        List<String> majors = applicationService.getApplicationMajors();
        return Results.success(majors);
    }
}
