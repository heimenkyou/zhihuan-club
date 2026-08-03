package cn.luowb.clubrecruitment.controller;

import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.ApplicationReqDTO;
import cn.luowb.clubrecruitment.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

}
