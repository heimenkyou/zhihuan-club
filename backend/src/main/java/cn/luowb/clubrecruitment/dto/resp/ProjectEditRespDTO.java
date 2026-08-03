package cn.luowb.clubrecruitment.dto.resp;

import cn.luowb.clubrecruitment.dto.TeamDivisionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 项目详情编辑回显响应参数
 */
@Data
public class ProjectEditRespDTO {
    /**
     * 关联项目id
     */
    @Schema(description = "关联项目id")
    private Long projectId;

    /**
     * 项目分类(如: Web开发)
     */
    @Schema(description = "项目分类")
    private String category;

    /**
     * 封面图片URL
     */
    @Schema(description = "封面图片URL")
    private String coverImage;

    /**
     * 项目标题
     */
    @Schema(description = "项目标题")
    private String title;

    /**
     * 简要介绍
     */
    @Schema(description = "简要介绍")
    private String briefIntro;

    /**
     * 技术栈标签数组["React", "Node.js"]
     */
    @Schema(description = "技术栈标签数组")
    private List<String> techStackTags;

    /**
     * 项目时间范围
     */
    @Schema(description = "项目时间范围")
    private String timeRange;

    @Schema(description = "轮播图片URL列表")
    private List<String> imageUrls;

    /**
     * Markdown格式的项目介绍
     */
    @Schema(description = "Markdown格式的项目介绍")
    private String descriptionMd;

    /**
     * 团队成员分工[{"name": "张三", "role": "前端开发"}]
     */
    @Schema(description = "团队成员分工")
    private List<TeamDivisionDTO> teamDivisions;

    /**
     * 奖项列表
     */
    @Schema(description = "奖项列表")
    private List<AwardRespDTO> awards;
}
