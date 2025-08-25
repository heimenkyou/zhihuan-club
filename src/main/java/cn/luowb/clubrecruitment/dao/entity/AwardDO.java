package cn.luowb.clubrecruitment.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 奖项信息表
 *
 * @TableName award
 */
@TableName(value = "award")
@Data
public class AwardDO {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 竞赛名称(如蓝桥杯、ACM/ICPC)
     */
    @TableField(value = "competition_name")
    private String competitionName;

    /**
     * 竞赛级别（校级/省级/国家级）
     */
    @TableField(value = "competition_level")
    private String competitionLevel;

    /**
     * 奖项级别（一等奖/二等奖/优胜奖等）
     */
    @TableField(value = "award_level")
    private String awardLevel;

    /**
     * 获奖人(JSON数组格式存储人名)
     */
    @TableField(value = "winners")
    private String winners;

    /**
     * 获奖年份(用于分类展示)
     */
    @TableField(value = "year")
    private Integer year;

    /**
     * 获奖时间(精确到月份)
     */
    @TableField(value = "award_date")
    private LocalDate awardDate;

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