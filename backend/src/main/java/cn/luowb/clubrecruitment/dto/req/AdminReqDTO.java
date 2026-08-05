package cn.luowb.clubrecruitment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 管理员账户添加请求参数
 */
@Data
public class AdminReqDTO {
    /** 创建管理员时必填字段的校验分组。 */
    public interface Create {
    }

    /**
     * 管理员登录用户名，唯一约束
     */
    @Schema(description = "管理员登录用户名，唯一约束")
    @NotBlank(message = "用户名不能为空", groups = Create.class)
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空", groups = Create.class)
    private String password;

    /**
     * normal(普通管理员)/super(超级管理员)/submitter(内容提交员)
     */
    @Schema(description = "normal(普通管理员)/super(超级管理员)/submitter(内容提交员)")
    @Pattern(regexp = "^(normal|super|submitter)?$", message = "管理员角色不合法")
    private String role;

}
