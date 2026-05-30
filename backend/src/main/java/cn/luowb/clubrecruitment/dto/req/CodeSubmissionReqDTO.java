package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 代码提交请求参数
 */
@Data
@Schema(description = "代码提交请求参数")
public class CodeSubmissionReqDTO {
    @NotBlank(message = "学号不能为空")
    @Schema(description = "学号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String studentId;

    @NotBlank(message = "项目描述不能为空")
    @Schema(description = "项目描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @NotNull(message = "代码压缩包不能为空")
    @Schema(description = "代码压缩包文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile codeFile;

    @NotNull(message = "演示视频不能为空")
    @Schema(description = "演示视频文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile videoFile;
}
