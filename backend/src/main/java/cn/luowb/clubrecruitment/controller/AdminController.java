package cn.luowb.clubrecruitment.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.AdminLoginReqDTO;
import cn.luowb.clubrecruitment.dto.req.AdminReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.AdminLoginRespDTO;
import cn.luowb.clubrecruitment.dto.resp.AdminPageRespDTO;
import cn.luowb.clubrecruitment.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "管理员")
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "登录")
    @PostMapping("/auth/login")
    public Result<AdminLoginRespDTO> login(@RequestBody @Valid AdminLoginReqDTO requestParam) {
        AdminLoginRespDTO adminLoginRespDTO = adminService.login(requestParam);
        log.info("管理员登录成功: {}", requestParam.getUsername());
        return Results.success(adminLoginRespDTO);
    }

    @Operation(summary = "登出")
    @PostMapping("/auth/logout")
    public Result<Void> logout() {
        adminService.logout();
        return Results.success();
    }

    @Operation(summary = "添加管理员")
    @SaCheckRole("super")
    @PostMapping("/admins")
    public Result<Void> add(@RequestBody @Validated({AdminReqDTO.Create.class, Default.class}) AdminReqDTO requestParam) {
        adminService.add(requestParam);
        return Results.success();
    }

    @Operation(summary = "分页查询管理员")
    @SaCheckRole(value = {"super", "submitter"}, mode = SaMode.OR)
    @GetMapping("/admins")
    public Result<PageData<AdminPageRespDTO>> getAdminPage(@ParameterObject PageReqDTO requestParam) {
        log.debug("分页查询管理员: 第{}页{}条", requestParam.getCurrent(), requestParam.getSize());
        PageData<AdminPageRespDTO> adminPage = adminService.getAdminPage(requestParam);
        return Results.success(adminPage);
    }

    @Operation(summary = "删除管理员")
    @SaCheckRole("super")
    @DeleteMapping("/admins/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("删除管理员: id={}", id);
        adminService.delete(id);
        return Results.success();
    }

    @Operation(summary = "更新管理员")
    @SaCheckRole("super")
    @PutMapping("/admins/{id}")
    public Result<Void> update(@RequestBody @Valid AdminReqDTO requestParam, @PathVariable Long id) {
        adminService.update(requestParam, id);
        return Results.success();
    }

    @Operation(summary = "获取当前管理员信息")
    @GetMapping("/auth/me")
    public Result<AdminPageRespDTO> getAdminInfo() {
        AdminPageRespDTO adminInfo = adminService.getAdminInfo();
        return Results.success(adminInfo);
    }

    @Operation(summary = "查询指定管理员信息")
    @SaCheckRole(value = {"super", "submitter"}, mode = SaMode.OR)
    @GetMapping("/admins/{id}")
    public Result<AdminPageRespDTO> getAdminInfo(@PathVariable Long id) {
        AdminPageRespDTO adminInfo = adminService.getAdminInfo(id);
        return Results.success(adminInfo);
    }

    @Operation(summary = "更新当前管理员资料")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    @PutMapping("/auth/me")
    public Result<Void> updateCurrent(@RequestBody @Valid AdminReqDTO requestParam) {
        adminService.updateCurrent(requestParam);
        return Results.success();
    }
}
