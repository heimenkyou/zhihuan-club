package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户留言表
 *
 * @TableName message
 */
@TableName(value = "message")
@Data
@Accessors(chain = true)
public class MessageDO {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 留言内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 点赞数量
     */
    @TableField(value = "like_count")
    private Integer likeCount;

    /**
     * 用户IP地址，用于防刷赞和安全审计
     */
    @TableField(value = "ip_address")
    private String ipAddress;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}