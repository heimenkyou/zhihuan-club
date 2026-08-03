package cn.luowb.clubrecruitment.common.context;

/**
 * 当前请求的客户端 IP 上下文。
 */
public final class IPContext {

    private static final ThreadLocal<String> IP_THREAD_LOCAL = new ThreadLocal<>();

    private IPContext() {
    }

    /**
     * 获取当前请求的客户端 IP。
     *
     * @return 客户端 IP
     */
    public static String getIp() {
        return IP_THREAD_LOCAL.get();
    }

    /**
     * 设置用户 IP
     *
     * @param ip 用户 IP
     */
    public static void setIp(String ip) {
        IP_THREAD_LOCAL.set(ip);
    }

    /**
     * 清理用户 IP
     */
    public static void removeIp() {
        IP_THREAD_LOCAL.remove();
    }
}
