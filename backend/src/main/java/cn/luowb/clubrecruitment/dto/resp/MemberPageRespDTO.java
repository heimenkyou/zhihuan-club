package cn.luowb.clubrecruitment.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "成员分页/详情返回实体")
public class MemberPageRespDTO {
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
     * QQ号
     */
    @Schema(description = "QQ号")
    @JsonProperty("QQNumber")
    private String QQNumber;

    /**
     * 所属部门
     */
    @Schema(description = "所属部门")
    private String department;

    /**
     * 加入年份
     */
    @Schema(description = "加入年份")
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
