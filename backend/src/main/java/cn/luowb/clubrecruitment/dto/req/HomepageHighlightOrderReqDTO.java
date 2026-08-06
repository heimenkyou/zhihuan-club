package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 高光排序请求参数。
 */
@Data
@Schema(description = "高光排序请求参数")
public class HomepageHighlightOrderReqDTO {
    @Schema(description = "目标顺序的高光ID列表（须与当前全部记录一致）")
    @NotEmpty(message = "排序列表不能为空")
    private List<Long> ids;
}
