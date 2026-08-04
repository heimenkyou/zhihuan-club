package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 管理员账户添加请求参数
 */
@Data
public class AdminReqDTO {
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

    /**
     * normal(普通管理员)/super(超级管理员)/submitter(内容提交员)
     */
    @Schema(description = "normal(普通管理员)/super(超级管理员)/submitter(内容提交员)")
    private String role;

}
