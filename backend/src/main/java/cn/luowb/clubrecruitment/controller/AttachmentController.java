package cn.luowb.clubrecruitment.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.luowb.clubrecruitment.common.result.PageData;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.web.Results;
import cn.luowb.clubrecruitment.dto.req.AttachmentUploadTokenReqDTO;
import cn.luowb.clubrecruitment.dto.req.PageReqDTO;
import cn.luowb.clubrecruitment.dto.resp.AttachmentRespDTO;
import cn.luowb.clubrecruitment.dto.resp.AttachmentUploadTokenRespDTO;
import cn.luowb.clubrecruitment.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端附件接口。
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/attachments")
@Tag(name = "附件管理")
public class AttachmentController {
    private final AttachmentService attachmentService;

    /**
     * 申请图片直传凭证。
     *
     * @param requestParam 文件元数据
     * @return 上传凭证
     */
    @PostMapping("/upload-token")
    @Operation(summary = "申请图片直传凭证")
    @SaCheckRole(value = {"normal", "super", "submitter"}, mode = SaMode.OR)
    public Result<AttachmentUploadTokenRespDTO> createUploadToken(
            @RequestBody @Valid AttachmentUploadTokenReqDTO requestParam) {
        return Results.success(attachmentService.createUploadToken(requestParam));
    }

    /**
     * 确认附件已上传。
     *
     * @param id 附件ID
     * @return 附件信息
     */
    @PostMapping("/{id}/complete")
    @Operation(summary = "确认附件已上传")
    @SaCheckRole(value = {"normal", "super", "submitter"}, mode = SaMode.OR)
    public Result<AttachmentRespDTO> complete(@PathVariable Long id) {
        return Results.success(attachmentService.complete(id));
    }

    /**
     * 分页查询可用图片。
     *
     * @param requestParam 分页参数
     * @return 附件分页
     */
    @GetMapping
    @Operation(summary = "分页查询可用图片")
    @SaCheckRole(value = {"normal", "super", "submitter"}, mode = SaMode.OR)
    public Result<PageData<AttachmentRespDTO>> page(@ParameterObject PageReqDTO requestParam) {
        return Results.success(attachmentService.getPage(requestParam));
    }

    /**
     * 删除图片。
     *
     * @param id 附件ID
     * @return 空结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除图片")
    @SaCheckRole(value = {"normal", "super"}, mode = SaMode.OR)
    public Result<Void> delete(@PathVariable Long id) {
        attachmentService.delete(id);
        return Results.success();
    }
}
