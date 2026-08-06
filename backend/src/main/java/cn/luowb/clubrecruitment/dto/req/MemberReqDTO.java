package cn.luowb.clubrecruitment.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "成员新增/编辑参数")
public class MemberReqDTO {

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
    @NotBlank(message = "专业名称不能为空")
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
     * 所属部门
     */
    @Schema(description = "所属部门")
    @NotBlank(message = "所属部门不能为空")
    private String department;

    /**
     * 加入年份
     */
    @Schema(description = "加入年份")
    @NotNull(message = "加入年份不能为空")
    private Integer joinYear;

    /**
     * 成员状态：active/inactive
     */
    @Schema(description = "成员状态：active/inactive")
    private String status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
