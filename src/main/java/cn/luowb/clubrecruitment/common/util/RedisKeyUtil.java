package cn.luowb.clubrecruitment.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyUtil {

    // 自动注入 spring.application.name 项目前缀, 避免不同项目冲突
    @Value("${spring.application.name}")
    private String projectPrefix;

    // 点赞 key 前缀
    private static final String MESSAGE_LIKE = "like:message:";

    // 点赞防刷时间
    public static final long LIKE_INTERVAL_SECONDS = 60*5; // 秒

    // 构建 key
    public String buildMessageLikeKey(Long messageId, String ip) {
        return projectPrefix + ":" + MESSAGE_LIKE + messageId + ":ip:" + ip;
    }
}
