package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户报名表
 *
 * @TableName application
 */
@TableName(value = "application")
@Data
@Accessors(chain = true)
public class ApplicationDO {
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
     * 电子邮箱
     */
    @TableField(value = "qq_number")
    private String QQNumber;

    /**
     * 第一意向部门
     */
    @TableField(value = "department")
    private String department;

    /**
     * 第二意向部门
     */
    @TableField(value = "second_department")
    private String secondDepartment;

    /**
     * 兴趣方向
     */
    @TableField(value = "interests")
    private String interests;

    /**
     * 个人介绍
     */
    @TableField(value = "introduction")
    private String introduction;

    /**
     * 加入原因
     */
    @TableField(value = "reason")
    private String reason;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}