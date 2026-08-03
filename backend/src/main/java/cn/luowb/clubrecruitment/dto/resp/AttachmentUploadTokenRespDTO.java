package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 附件直传凭证响应参数。
 */
@Data
@Builder
@Schema(description = "附件直传凭证响应参数")
public class AttachmentUploadTokenRespDTO {
    private Long id;
    private String key;
    private String token;
    private String uploadUrl;
}
