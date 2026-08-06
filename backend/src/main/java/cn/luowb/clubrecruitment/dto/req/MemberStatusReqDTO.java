package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "成员状态切换参数")
public class MemberStatusReqDTO {

    /**
     * 成员状态：active/inactive
     */
    @Schema(description = "成员状态：active/inactive")
    @NotBlank(message = "状态不能为空")
    private String status;
}
