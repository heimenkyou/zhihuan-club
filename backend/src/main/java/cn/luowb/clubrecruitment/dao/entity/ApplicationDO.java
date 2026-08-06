package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
     * 招新批次年份
     */
    @TableField(value = "application_year")
    private Integer applicationYear;

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
     * 第一阶段尝试方向（JSON 数组字符串）
     */
    @TableField(value = "initial_directions")
    private String initialDirections;

    /**
     * 个人介绍
     */
    @TableField(value = "introduction")
    private String introduction;

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