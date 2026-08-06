package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * 添加/编辑活动高光请求参数。
 */
@Data
@Schema(description = "活动高光请求参数")
public class HomepageHighlightActivityReqDTO {
    @Schema(description = "活动标题")
    @NotBlank(message = "请输入活动标题")
    private String title;

    @Schema(description = "活动简介")
    @NotBlank(message = "请输入活动简介")
    private String description;

    @Schema(description = "封面图片URL")
    @NotBlank(message = "请选择封面图片")
    private String coverImage;

    @Schema(description = "活动日期（可空）")
    private LocalDate activityDate;
}
