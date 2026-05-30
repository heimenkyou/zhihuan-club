package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分页查询参数")
public class PageReqDTO {
    /**
     * 当前页码
     */
    @Schema(description = "当前页码",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private long current = 1;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小",
            example = "10",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private long size = 10;
}
