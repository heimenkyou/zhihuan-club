package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目基本信息响应数据
 */
@Data
public class ProjectRespDTO {
    /**
     * ID
     */
    @Schema(description = "ID")
    private Long id;

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
     * 开发团队成员["张三", "李四"]
     */
    @Schema(description = "开发团队成员")
    private List<String> teamMembers;

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