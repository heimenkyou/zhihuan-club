package cn.luowb.clubrecruitment.common.context;

import cn.dev33.satoken.stp.StpUtil;

/**
 * 当前管理员会话信息访问入口。
 */
public final class UserContext {
    private static final String USERNAME_KEY = "username";

    private UserContext() {
    }

    /**
     * 获取当前管理员 ID。
     *
     * @return 未登录时返回 {@code null}
     */
    public static Long getUserId() {
        return StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
    }

    /**
     * 获取当前管理员用户名。
     *
     * @return 未登录时返回 {@code null}
     */
    public static String getUsername() {
        return StpUtil.isLogin() ? StpUtil.getSession().getString(USERNAME_KEY) : null;
    }

    /**
     * 更新当前会话中的管理员用户名。
     *
     * @param username 管理员用户名
     */
    public static void setUsername(String username) {
        StpUtil.getSession().set(USERNAME_KEY, username);
    }
}
