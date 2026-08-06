package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 首页高光配置记录。
 */
@Data
@Accessors(chain = true)
@TableName("homepage_highlight")
public class HomepageHighlightDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 类型：project/activity
     */
    private String itemType;

    /**
     * 项目ID（项目类型使用）
     */
    private Long projectId;

    /**
     * 标题（活动类型使用）
     */
    private String title;

    /**
     * 简介（活动类型使用）
     */
    private String description;

    /**
     * 封面图片URL（活动类型使用）
     */
    private String coverImage;

    /**
     * 活动日期（活动类型使用，可空）
     */
    private LocalDate activityDate;

    /**
     * 展示顺序
     */
    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
