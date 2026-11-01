package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 成员批量匹配请求。
 */
@Data
@Schema(description = "成员批量匹配请求")
public class MemberBatchMatchReqDTO {

    /**
     * 待匹配的学号列表
     */
    @Schema(description = "待匹配的学号列表")
    @NotEmpty(message = "学号列表不能为空")
    @Size(max = 200, message = "单次最多匹配 200 个学号")
    private List<String> studentIds;
}
