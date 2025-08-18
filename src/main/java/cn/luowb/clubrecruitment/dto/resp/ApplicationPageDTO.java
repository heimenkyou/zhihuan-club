package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "报名分页查询返回实体")
public class ApplicationPageDTO {
    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 学号
     */
    @Schema(description = "学号")
    private String studentId;

    /**
     * 班级名称
     */
    @Schema(description = "班级名称")
    private String className;

    /**
     * 专业名称
     */
    @Schema(description = "专业名称")
    private String major;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String phone;

    /**
     * 电子邮箱
     */
    @Schema(description = "电子邮箱")
    private String QQNumber;

    /**
     * 意向部门
     */
    @Schema(description = "意向部门")
    private String department;

    /**
     * 个人介绍
     */
    @Schema(description = "个人介绍")
    private String introduction;

    /**
     * 加入原因
     */
    @Schema(description = "加入原因")
    private String reason;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Date updateTime;
}