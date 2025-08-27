package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 管理员账户信息表，存储系统管理员身份凭证
 *
 * @TableName admin
 */
@TableName(value = "admin")
@Data
@Accessors(chain = true)
public class AdminDO {
    /**
     * 管理员唯一标识，自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 管理员登录用户名，唯一约束
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码散列值
     */
    @TableField(value = "password_hash")
    private String passwordHash;

    /**
     * normal(普通管理员)/super(超级管理员)
     */
    @TableField(value = "role")
    private String role;

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