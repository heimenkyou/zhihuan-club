package cn.luowb.clubrecruitment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "团队成员分工")
public class TeamDivisionDTO {
    @Schema(description = "成员姓名")
    private String name;

    @Schema(description = "成员角色")
    private String role;
}