package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目基本信息表
 *
 * @TableName project
 */
@TableName(value = "project")
@Data
public class ProjectDO {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目分类(如: Web开发)
     */
    @TableField(value = "category")
    private String category;

    /**
     * 封面图片URL
     */
    @TableField(value = "cover_image")
    private String coverImage;

    /**
     * 项目标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 简要介绍
     */
    @TableField(value = "brief_intro")
    private String briefIntro;

    /**
     * 技术栈标签数组["React", "Node.js"]
     */
    @TableField(value = "tech_stack_tags")
    private String techStackTags;

    /**
     * 开发团队成员[{"name": "张三", "role": "前端开发"}]
     */
    @TableField(value = "team_members")
    private String teamMembers;

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