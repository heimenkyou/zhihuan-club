package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目详情表
 *
 * @TableName project_detail
 */
@TableName(value = "project_detail")
@Data
public class ProjectDetailDO {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联项目id
     */
    @TableField(value = "project_id")
    private Long projectId;

    /**
     * 项目时间范围
     */
    @TableField(value = "time_range")
    private String timeRange;

    /**
     * 媒体资源[{"type": "image", "url": "xxx"}, {"type": "video", "url": "yyy"}]
     */
    @TableField(value = "media_resources")
    private String mediaResources;

    /**
     * 详细技术栈标签
     */
    @TableField(value = "tech_stack_detail")
    private String techStackDetail;

    /**
     * Markdown格式的项目介绍
     */
    @TableField(value = "description_md")
    private String descriptionMd;

    /**
     * 团队成员分工[{"name": "张三", "role": "前端开发"}]
     */
    @TableField(value = "team_division")
    private String teamDivision;

    /**
     * 奖项ID列表[1, 2, 3]
     */
    @TableField(value = "award_ids")
    private String awardIds;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}