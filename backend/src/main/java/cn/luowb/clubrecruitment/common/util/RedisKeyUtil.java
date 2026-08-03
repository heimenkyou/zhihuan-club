package cn.luowb.clubrecruitment.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyUtil {
    // 点赞防刷时间
    @Value("${app.like-interval-seconds:3600}")
    public long likeIntervalSeconds;
    // 自动注入 spring.application.name 项目前缀, 避免不同项目冲突
    @Value("${spring.application.name}")
    private String projectPrefix;

    // 留言点赞
    public String buildMessageLikeKey(Long messageId, String ip) {
        return projectPrefix + ":like:message:" + messageId + ":ip:" + ip;
    }

    public String buildMajorMappingKey() {
        return projectPrefix + ":major:mapping";
    }
}
