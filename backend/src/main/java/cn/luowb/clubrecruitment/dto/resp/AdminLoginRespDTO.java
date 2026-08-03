package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 管理员账户登录响应参数
 */
@Data
@Accessors(chain = true)
public class AdminLoginRespDTO {
    /**
     * 管理员唯一标识，自增主键
     */
    @Schema(description = "管理员唯一标识，自增主键")
    private Long id;

    /**
     * 管理员登录用户名，唯一约束
     */
    @Schema(description = "管理员登录用户名，唯一约束")
    private String username;

    /**
     * normal(普通管理员)/super(超级管理员)
     */
    @Schema(description = "normal(普通管理员)/super(超级管理员)")
    private String role;

    /**
     * 登录凭证
     */
    @Schema(description = "登录凭证")
    private String token;
}
