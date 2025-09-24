package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "报名信息分页查询参数")
public class ApplicationPageReqDTO {
    /**
     * 当前页码
     */
    @Schema(description = "当前页码",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private long current = 1;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小",
            example = "10",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private long size = 10;

    @Schema(description = "姓名")
    private String name;

    /**
     * 学号
     */
    @Schema(description = "学号")
    private String studentId;

    /**
     * 专业名称列表
     */
    @Schema(description = "专业名称列表（可重复使用此参数）", example = "物联网工程,软件工程")
    private List<String> majors;

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
}
