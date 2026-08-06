package cn.luowb.clubrecruitment.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
     * 招新批次年份
     */
    @Schema(description = "招新批次年份")
    private Integer applicationYear;

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
     * 第一阶段尝试方向
     */
    @Schema(description = "第一阶段尝试方向")
    private List<String> initialDirections;

    /**
     * 个人介绍
     */
    @Schema(description = "个人介绍")
    private String introduction;

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
