package cn.luowb.clubrecruitment.common.context;

/**
 * 当前请求的访客令牌上下文。
 *
 * <p>访客令牌由后端通过 HttpOnly Cookie 下发，这里仅缓存当前请求携带的原始值，
 * 供留言的删除鉴权与点赞状态判断使用。</p>
 */
public final class VisitorTokenContext {

    /** 访客令牌 Cookie 名称。 */
    public static final String COOKIE_NAME = "visitor_token";

    /** 访客令牌 Cookie 有效期（秒）：一年。 */
    public static final int COOKIE_MAX_AGE_SECONDS = 31536000;

    private static final ThreadLocal<String> TOKEN_THREAD_LOCAL = new ThreadLocal<>();

    private VisitorTokenContext() {
    }

    /**
     * 获取当前请求的访客令牌。
     *
     * @return 访客令牌，缺失时返回 {@code null}
     */
    public static String getToken() {
        return TOKEN_THREAD_LOCAL.get();
    }

    /**
     * 设置当前请求的访客令牌。
     *
     * @param token 访客令牌
     */
    public static void setToken(String token) {
        TOKEN_THREAD_LOCAL.set(token);
    }

    /**
     * 清理当前请求的访客令牌。
     */
    public static void removeToken() {
        TOKEN_THREAD_LOCAL.remove();
    }
}
