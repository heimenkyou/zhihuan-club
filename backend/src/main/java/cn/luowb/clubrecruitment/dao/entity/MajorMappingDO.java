package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 专业代号与名称映射表
 *
 * @TableName major_mapping
 */
@TableName(value = "major_mapping")
@Data
public class MajorMappingDO {
    /**
     * 专业代号，4位数字
     */
    @TableId(value = "code")
    private String code;

    /**
     * 专业简称，如 环工、环科、视传 等
     */
    @TableField(value = "short_name")
    private String shortName;

    /**
     * 专业全称，来自官方专业列表
     */
    @TableField(value = "full_name")
    private String fullName;
}