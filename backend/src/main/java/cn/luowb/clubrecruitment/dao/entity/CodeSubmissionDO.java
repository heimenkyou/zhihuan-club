package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 代码提交记录表
 *
 * @TableName code_submission
 */
@TableName(value = "code_submission")
@Data
@Accessors(chain = true)
public class CodeSubmissionDO {
    /**
     * 提交ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学号
     */
    @TableField(value = "student_id")
    private String studentId;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 项目描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}