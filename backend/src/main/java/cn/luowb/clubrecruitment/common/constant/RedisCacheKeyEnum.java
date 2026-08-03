package cn.luowb.clubrecruitment.common.constant;

import lombok.AllArgsConstructor;

/** Redis 缓存键模板。 */
@AllArgsConstructor
public enum RedisCacheKeyEnum {
    /** 留言点赞冷却。 */
    MESSAGE_LIKE_KEY("club:like:message:%s:ip:%s"),

    /** 专业映射缓存。 */
    MAJOR_MAPPING_KEY("club:major:mapping");

    private final String template;

    /**
     * 按参数生成缓存键。
     *
     * @param params 模板参数
     * @return 缓存键
     */
    public String getKey(Object... params) {
        return String.format(template, params);
    }
}
