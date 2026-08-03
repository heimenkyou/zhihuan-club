package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 附件记录。
 */
@Data
@Accessors(chain = true)
@TableName("attachment")
public class AttachmentDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String objectKey;
    private String legacyUrl;
    private String originalName;
    private String type;
    private String mimeType;
    private Long size;
    private String status;
    private String refType;
    private Long refId;
    private String usage;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
