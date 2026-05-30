package cn.luowb.clubrecruitment.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 用户登录信息存储上下文
 */
public class IPContext {

    /**
     * <a href="https://github.com/alibaba/transmittable-thread-local" />
     */
    private static final ThreadLocal<String> IP_THREAD_LOCAL = new TransmittableThreadLocal<>();

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