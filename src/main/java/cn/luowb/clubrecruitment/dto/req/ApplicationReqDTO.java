package cn.luowb.clubrecruitment.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

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
    @JsonProperty("QQNumber")
    private String QQNumber;

    /**
     * 第一意向部门
     */
    @Schema(description = "第一意向部门")
    private String department;

    /**
     * 第二意向部门
     */
    @Schema(description = "第二意向部门")
    private String secondDepartment;

    /**
     * 兴趣方向
     */
    @Schema(description = "兴趣方向")
    private List<String> interests;

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