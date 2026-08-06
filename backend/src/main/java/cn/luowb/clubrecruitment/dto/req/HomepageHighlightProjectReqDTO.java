package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 添加项目高光请求参数。
 */
@Data
@Schema(description = "添加项目高光请求参数")
public class HomepageHighlightProjectReqDTO {
    @Schema(description = "项目ID")
    @NotNull(message = "请选择项目")
    private Long projectId;
}
