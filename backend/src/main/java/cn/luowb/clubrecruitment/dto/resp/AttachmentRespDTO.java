package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 附件响应参数。
 */
@Data
@Schema(description = "附件响应参数")
public class AttachmentRespDTO {
    private Long id;
    private String objectKey;
    private String originalName;
    private String mimeType;
    private Long size;
    private String status;
    private String url;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
