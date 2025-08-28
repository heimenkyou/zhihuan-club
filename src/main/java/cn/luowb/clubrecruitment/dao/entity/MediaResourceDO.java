package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 媒体资源表
 *
 * @TableName media_resource
 */
@TableName(value = "media_resource")
@Data
public class MediaResourceDO {
    /**
     * 资源ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 资源类型,image/video/audio
     */
    @TableField(value = "type")
    private String type;

    /**
     * 资源URL
     */
    @TableField(value = "url")
    private String url;

    /**
     * 资源引用ID
     */
    @TableField(value = "ref_id")
    private Long refId;

    /**
     * 资源引用类型,project/race_info
     */
    @TableField(value = "ref_type")
    private String refType;

    /**
     * 资源标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 资源描述
     */
    @TableField(value = "description")
    private String description;

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