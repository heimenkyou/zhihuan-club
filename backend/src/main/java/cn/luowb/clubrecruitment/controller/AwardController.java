package cn.luowb.clubrecruitment.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.AwardReqDTO;
import cn.luowb.clubrecruitment.dto.resp.AwardRespDTO;
import cn.luowb.clubrecruitment.service.AwardService;
import com.alibaba.fastjson2.JSONArray;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "奖项")
@RequestMapping("/api")
public class AwardController {
    private final AwardService awardService;

    @Operation(summary = "查询所有奖项")
    @GetMapping("/awards")
    public Result<List<AwardRespDTO>> list() {
        return Results.success(listAwards());
    }

    @Operation(summary = "后台查询所有奖项")
    @SaCheckRole(value = {"normal", "super", "submitter"}, mode = SaMode.OR)
    @GetMapping("/admin/awards")
    public Result<List<AwardRespDTO>> adminList() {
        return Results.success(listAwards());
    }

    private List<AwardRespDTO> listAwards() {
        List<AwardRespDTO> awardRespDTOS = awardService.list().stream()
                .map(each -> {
                    AwardRespDTO respDTO = BeanUtil.toBean(each, AwardRespDTO.class);
                    respDTO.setWinners(JSONArray.parseArray(each.getWinners(), String.class));
                    respDTO.setYear(each.getYear());
                    return respDTO;
                })
                .toList();
        log.debug("查询所有奖项, 数量={}", awardRespDTOS.size());
        return awardRespDTOS;
    }

    @Operation(summary = "添加奖项")
    @SaCheckRole(value = {"normal", "super", "submitter"}, mode = SaMode.OR)
    @PostMapping("/admin/awards")
    public Result<Void> add(@RequestBody @Validated({AwardReqDTO.Create.class, Default.class}) AwardReqDTO awardReqDTO) {
        log.debug("添加奖项, awardReqDTO={}", awardReqDTO);
        awardService.add(awardReqDTO);
        return Results.success();
    }

    @Operation(summary = "删除奖项")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    @DeleteMapping("/admin/awards/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.debug("删除奖项, id={}", id);
        awardService.delete(id);
        return Results.success();
    }

    @Operation(summary = "更新奖项")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    @PutMapping("/admin/awards/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Valid AwardReqDTO awardReqDTO) {
        log.debug("更新奖项, id={}, awardReqDTO={}", id, awardReqDTO);
        awardService.update(id, awardReqDTO);
        return Results.success();
    }
}
