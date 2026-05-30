package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 管理员账户登录请求参数
 */
@Data
public class AdminLoginReqDTO {

    /**
     * 管理员登录用户名，唯一约束
     */
    @Schema(description = "管理员登录用户名，唯一约束")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;
}