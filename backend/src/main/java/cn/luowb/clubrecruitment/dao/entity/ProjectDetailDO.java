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
     * 详细技术栈标签
     */
    @TableField(value = "tech_stack_tags")
    private String techStackTags;

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
     * 轮播图片URL列表
     */
    @TableField(value = "image_urls")
    private String imageUrls;

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
