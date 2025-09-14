package cn.luowb.clubrecruitment.controller;

import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dao.entity.MajorMappingDO;
import cn.luowb.clubrecruitment.service.MajorMappingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "专业")
public class MajorMappingController {
    private final MajorMappingService majorMappingService;

    @GetMapping("/public/majors")
    @Operation(summary = "获取专业代号与名称映射表")
    public Result<Map<String, MajorMappingDO>> list() {
        return Results.success(majorMappingService.majors());
    }
}
