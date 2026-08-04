package cn.luowb.clubrecruitment.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 管理员账户分页响应参数
 */
@Data
@Accessors(chain = true)
public class AdminPageRespDTO {
    /**
     * 管理员唯一标识，自增主键
     */
    @Schema(description = "管理员唯一标识")
    private Long id;

    /**
     * 管理员登录用户名，唯一约束
     */
    @Schema(description = "管理员登录用户名")
    private String username;

    /**
     * normal(普通管理员)/super(超级管理员)/submitter(内容提交员)
     */
    @Schema(description = "管理员角色")
    private String role;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
