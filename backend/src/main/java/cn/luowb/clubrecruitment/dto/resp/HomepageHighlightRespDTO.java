package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 首页高光统一响应数据。
 * 项目高光的 title/description/coverImage 动态读取项目表，活动高光读取自身字段。
 */
@Data
@Schema(description = "首页高光响应数据")
public class HomepageHighlightRespDTO {
    @Schema(description = "高光ID")
    private Long id;

    @Schema(description = "类型：project/activity")
    private String type;

    @Schema(description = "项目ID（活动为 null）")
    private Long projectId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "简介")
    private String description;

    @Schema(description = "封面图片URL")
    private String coverImage;

    @Schema(description = "活动日期（活动使用；项目统一为 null）")
    private LocalDate activityDate;
}
