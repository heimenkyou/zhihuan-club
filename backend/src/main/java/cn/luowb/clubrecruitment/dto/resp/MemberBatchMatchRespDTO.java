package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 成员批量匹配结果。
 */
@Data
@Schema(description = "成员批量匹配结果")
public class MemberBatchMatchRespDTO {

    /**
     * 学号
     */
    @Schema(description = "学号")
    private String studentId;

    /**
     * 已存在的成员，不存在时为空
     */
    @Schema(description = "已存在的成员，不存在时为空")
    private MemberPageRespDTO member;

    /**
     * 该学号的报名记录，按报名年份倒序
     */
    @Schema(description = "该学号的报名记录，按报名年份倒序")
    private List<ApplicationPageDTO> applications;
}
