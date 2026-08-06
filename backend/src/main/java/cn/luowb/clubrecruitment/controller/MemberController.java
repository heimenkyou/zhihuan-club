package cn.luowb.clubrecruitment.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.MemberPageReqDTO;
import cn.luowb.clubrecruitment.dto.req.MemberReqDTO;
import cn.luowb.clubrecruitment.dto.req.MemberStatusReqDTO;
import cn.luowb.clubrecruitment.dto.resp.MemberPageRespDTO;
import cn.luowb.clubrecruitment.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 成员后台管理接口。
 */
@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
@Tag(name = "成员管理")
@SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
public class MemberController {
    private final MemberService memberService;

    /**
     * 分页查询成员信息。
     *
     * @param requestParam 查询条件
     * @return 成员分页数据
     */
    @Operation(summary = "分页查询成员信息")
    @GetMapping
    public Result<PageData<MemberPageRespDTO>> list(@ParameterObject MemberPageReqDTO requestParam) {
        return Results.success(memberService.getMemberPage(requestParam));
    }

    /**
     * 查询成员详情。
     *
     * @param id 成员 ID
     * @return 成员详情
     */
    @Operation(summary = "查询成员详情")
    @GetMapping("/{id}")
    public Result<MemberPageRespDTO> detail(@PathVariable Long id) {
        return Results.success(memberService.getMemberDetail(id));
    }

    /**
     * 新增成员。
     *
     * @param requestParam 成员参数
     * @return 空响应
     */
    @Operation(summary = "新增成员")
    @PostMapping
    public Result<Void> create(@RequestBody @Valid MemberReqDTO requestParam) {
        memberService.createMember(requestParam);
        return Results.success();
    }

    /**
     * 编辑成员。
     *
     * @param id           成员 ID
     * @param requestParam 成员参数
     * @return 空响应
     */
    @Operation(summary = "编辑成员")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Valid MemberReqDTO requestParam) {
        memberService.updateMember(id, requestParam);
        return Results.success();
    }

    /**
     * 切换成员状态。
     *
     * @param id            成员 ID
     * @param requestParam  状态参数
     * @return 空响应
     */
    @Operation(summary = "切换成员状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody @Valid MemberStatusReqDTO requestParam) {
        memberService.updateMemberStatus(id, requestParam.getStatus());
        return Results.success();
    }
}
