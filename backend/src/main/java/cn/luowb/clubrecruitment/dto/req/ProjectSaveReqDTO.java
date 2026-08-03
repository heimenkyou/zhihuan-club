package cn.luowb.clubrecruitment.dto.req;

import cn.luowb.clubrecruitment.dto.TeamDivisionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 项目保存请求参数。
 */
@Data
@Schema(description = "项目保存请求参数")
public class ProjectSaveReqDTO {
    @Schema(description = "项目分类")
    @NotBlank(message = "请选择项目分类")
    private String category;

    @Schema(description = "封面图片URL")
    @NotBlank(message = "请设置封面图片")
    private String coverImage;

    @Schema(description = "项目标题")
    @NotBlank(message = "请输入项目标题")
    private String title;

    @Schema(description = "简要介绍")
    @NotBlank(message = "请输入项目简要介绍")
    private String briefIntro;

    @Schema(description = "技术栈标签")
    private List<String> techStackTags;

    @Schema(description = "项目时间范围")
    private String timeRange;

    @Schema(description = "轮播图片URL列表")
    @NotEmpty(message = "请至少添加一张轮播图")
    private List<String> imageUrls;

    @Schema(description = "Markdown格式的项目介绍")
    private String descriptionMd;

    @Schema(description = "团队成员分工")
    @NotEmpty(message = "请至少添加一名团队成员")
    private List<TeamDivisionDTO> teamDivision;

    @Schema(description = "奖项ID列表")
    private List<Long> awardIds;
}
