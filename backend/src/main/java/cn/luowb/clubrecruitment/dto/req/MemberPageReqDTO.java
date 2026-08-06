package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "成员分页查询参数")
public class MemberPageReqDTO {
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
}
