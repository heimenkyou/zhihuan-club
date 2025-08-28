package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "媒体上传请求参数")
public class MediaUploadReqDTO {
    @Schema(description = "媒体文件", requiredMode = Schema.RequiredMode.REQUIRED)
    private MultipartFile file;

    @Schema(description = "资源标题(可选)")
    private String title;

    @Schema(description = "资源描述(可选)")
    private String description;
}