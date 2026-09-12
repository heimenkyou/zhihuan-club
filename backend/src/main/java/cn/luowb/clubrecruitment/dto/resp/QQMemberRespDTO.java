package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * QQ 群机器人查询成员信息的响应。
 */
@Data
@Accessors(chain = true)
@Schema(description = "QQ 群机器人成员信息")
public class QQMemberRespDTO {
    /** QQ 号。 */
    @Schema(description = "QQ号")
    private String qqNumber;

    /** 班级名称。 */
    @Schema(description = "班级名称")
    private String className;

    /** 专业全称。 */
    @Schema(description = "专业全称")
    private String major;

    /** 专业简称。 */
    @Schema(description = "专业简称")
    private String majorShortName;

    /** 成员姓名。 */
    @Schema(description = "成员姓名")
    private String name;
}
