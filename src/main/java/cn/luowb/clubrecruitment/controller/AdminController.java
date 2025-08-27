package cn.luowb.clubrecruitment.controller;

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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "管理员")
public class AdminController {
    private final StringRedisTemplate redisTemplate;
    private final AdminService adminService;

    @Operation(summary = "登录")
    @PostMapping("/public/admins/login")
    public Result<AdminLoginRespDTO> login(@RequestBody AdminLoginReqDTO requestParam) {
        log.info("管理员登录，请求参数：{}", requestParam);
        AdminLoginRespDTO adminLoginRespDTO = adminService.login(requestParam);
        return Results.success(adminLoginRespDTO);
    }

    @Operation(summary = "登出")
    @PostMapping("/public/admins/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.replace("Bearer ", "");
        adminService.logout(token);
        return Results.success();
    }

    @Operation(summary = "添加管理员")
    @PostMapping("/admin/admins/add")
    public Result<Void> add(@RequestBody AdminReqDTO requestParam) {
        log.info("添加管理员: {}", requestParam);
        adminService.add(requestParam);
        return Results.success();
    }

    @Operation(summary = "分页查询管理员")
    @GetMapping("/admin/admins/page")
    public Result<PageData<AdminPageRespDTO>> getAdminPage(@ParameterObject PageReqDTO requestParam) {
        log.debug("分页查询管理员: 第{}页{}条", requestParam.getCurrent(), requestParam.getSize());
        PageData<AdminPageRespDTO> adminPage = adminService.getAdminPage(requestParam);
        return Results.success(adminPage);
    }

    @Operation(summary = "删除管理员")
    @DeleteMapping("/admin/admins/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("删除管理员: id={}", id);
        adminService.delete(id);
        return Results.success();
    }

    @Operation(summary = "更新管理员")
    @PutMapping("/admin/admins/{id}")
    public Result<Void> update(@RequestBody AdminReqDTO requestParam, @PathVariable Long id) {
        log.info("更新管理员: id={}, {}", id, requestParam);
        adminService.update(requestParam, id);
        return Results.success();
    }

    @Operation(summary = "获取当前管理员信息")
    @GetMapping("/admin/admins/me")
    public Result<AdminPageRespDTO> getAdminInfo() {
        AdminPageRespDTO adminInfo = adminService.getAdminInfo();
        return Results.success(adminInfo);
    }

    @Operation(summary = "查询指定管理员信息")
    @GetMapping("/admin/admins/{id}")
    public Result<AdminPageRespDTO> getAdminInfo(@PathVariable Long id) {
        AdminPageRespDTO adminInfo = adminService.getAdminInfo(id);
        return Results.success(adminInfo);
    }
}
