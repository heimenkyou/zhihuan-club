package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 附件直传凭证请求参数。
 */
@Data
@Schema(description = "附件直传凭证请求参数")
public class AttachmentUploadTokenReqDTO {
    @NotBlank(message = "原文件名不能为空")
    @Size(max = 255, message = "原文件名不能超过255个字符")
    @Schema(description = "原文件名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String originalName;

    @NotBlank(message = "MIME类型不能为空")
    @Size(max = 128, message = "MIME类型不能超过128个字符")
    @Schema(description = "MIME类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mimeType;

    @Positive(message = "文件大小必须大于0")
    @Schema(description = "文件大小，单位字节", requiredMode = Schema.RequiredMode.REQUIRED)
    private long size;
}
