package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 附件重命名请求参数。
 */
@Data
@Schema(description = "附件重命名请求参数")
public class AttachmentRenameReqDTO {
    @NotBlank(message = "附件名称不能为空")
    @Size(max = 255, message = "附件名称不能超过255个字符")
    @Schema(description = "附件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String originalName;
}
