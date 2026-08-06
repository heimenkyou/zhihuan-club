package cn.luowb.clubrecruitment.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "报名参数")
public class ApplicationReqDTO {

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 学号
     */
    @Schema(description = "学号")
    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "\\d{11}", message = "学号格式错误")
    private String studentId;

    /**
     * 专业名称
     */
    @Schema(description = "专业名称")
    @NotBlank(message = "专业名称不能为空")
    private String major;

    /**
     * 班级，由前端依据学号识别后传入
     */
    @Schema(description = "班级")
    @NotBlank(message = "班级不能为空")
    private String className;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "\\d{11}", message = "手机号格式错误")
    private String phone;

    /**
     * QQ号
     */
    @Schema(description = "QQ号")
    @JsonProperty("QQNumber")
    @NotBlank(message = "QQ号不能为空")
    @Pattern(regexp = "\\d+", message = "QQ号格式错误")
    private String QQNumber;

    /**
     * 第一意向部门
     */
    @Schema(description = "第一意向部门")
    @NotBlank(message = "第一意向部门不能为空")
    private String department;

    /**
     * 第二意向部门
     */
    @Schema(description = "第二意向部门")
    private String secondDepartment;

    /**
     * 第一阶段尝试方向
     */
    @Schema(description = "第一阶段尝试方向")
    @NotEmpty(message = "第一阶段尝试方向不能为空")
    private List<String> initialDirections;

    /**
     * 个人介绍
     */
    @Schema(description = "个人介绍")
    @NotBlank(message = "个人介绍不能为空")
    private String introduction;
}
