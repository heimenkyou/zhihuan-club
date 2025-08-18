package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "报名参数")
public class ApplicationReqDTO {

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
     * QQ号
     */
    @Schema(description = "QQ号")
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
}