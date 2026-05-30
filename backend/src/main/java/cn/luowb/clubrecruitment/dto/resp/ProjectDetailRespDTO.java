package cn.luowb.clubrecruitment.dto.resp;

import cn.luowb.clubrecruitment.dto.TeamDivisionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目详情响应参数
 */
@Data
public class ProjectDetailRespDTO {
    /**
     * 关联项目id
     */
    @Schema(description = "关联项目id")
    private Long projectId;

    /**
     * 项目标题
     */
    @Schema(description = "项目标题")
    private String title;

    /**
     * 项目分类(如: Web开发)
     */
    @Schema(description = "项目分类")
    private String category;

    /**
     * 项目时间范围
     */
    @Schema(description = "项目时间范围")
    private String timeRange;

    /**
     * 媒体资源[{"id": 1, "type": "image", "url": "xxx", "title":"应用主界面", "description":"xxx"}, ...',
     */
    @Schema(description = "媒体资源")
    private List<MediaResourceRespDTO> mediaResources;

    /**
     * 详细技术栈标签
     */
    @Schema(description = "详细技术栈标签")
    private List<String> techStackTags;

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

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}