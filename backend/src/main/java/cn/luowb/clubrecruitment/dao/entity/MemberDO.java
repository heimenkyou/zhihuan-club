package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 社团成员表
 *
 * @TableName member
 */
@TableName(value = "member")
@Data
@Accessors(chain = true)
public class MemberDO {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 学号
     */
    @TableField(value = "student_id")
    private String studentId;

    /**
     * 班级名称
     */
    @TableField(value = "class_name")
    private String className;

    /**
     * 专业名称
     */
    @TableField(value = "major")
    private String major;

    /**
     * 联系电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * QQ号
     */
    @TableField(value = "qq_number")
    private String QQNumber;

    /**
     * 所属部门
     */
    @TableField(value = "department")
    private String department;

    /**
     * 加入年份
     */
    @TableField(value = "join_year")
    private Integer joinYear;

    /**
     * 成员状态：active/inactive
     */
    @TableField(value = "status")
    private String status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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
