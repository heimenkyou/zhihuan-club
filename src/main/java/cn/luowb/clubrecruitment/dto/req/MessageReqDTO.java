package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MessageReqDTO {
    @Schema(description = "用户昵称",
            example = "张三(测试用户)",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickname;

    @Schema(description = "留言内容",
            example = "大家好, 这是一条测试留言",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;
}
