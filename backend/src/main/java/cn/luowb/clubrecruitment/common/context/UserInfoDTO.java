package cn.luowb.clubrecruitment.common.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录用户信息实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色
     */
    private String role;

    /**
     * 登录凭证
     */
    private String token;
}