package cn.luowb.clubrecruitment.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoForRedisTemplate;
import cn.dev33.satoken.stp.StpUtil;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 管理后台登录校验配置。
 */
@Configuration
public class SaTokenConfiguration implements WebMvcConfigurer {

    /**
     * 为 Sa-Token 会话键添加应用命名空间，避免与其他服务共用 Redis 时冲突。
     *
     * @param connectionFactory Redis 连接工厂
     * @return 带命名空间的 Sa-Token Redis DAO
     */
    @Bean
    @Primary
    public SaTokenDao saTokenDao(RedisConnectionFactory connectionFactory) {
        ClubSaTokenDao dao = new ClubSaTokenDao();
        dao.init(connectionFactory);
        return dao;
    }

    /**
     * 保护全部后台接口，仅允许登录接口匿名访问。
     *
     * @param registry MVC 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()))
                .addPathPatterns("/api/admin/**")
                .excludePathPatterns("/api/admin/auth/login");
    }

    private static class ClubSaTokenDao extends SaTokenDaoForRedisTemplate {
        private static final String KEY_PREFIX = "club:";

        @Override
        public String get(String key) {
            return super.get(prefix(key));
        }

        @Override
        public void set(String key, String value, long timeout) {
            super.set(prefix(key), value, timeout);
        }

        @Override
        public void update(String key, String value) {
            super.update(prefix(key), value);
        }

        @Override
        public void delete(String key) {
            super.delete(prefix(key));
        }

        @Override
        public long getTimeout(String key) {
            return super.getTimeout(prefix(key));
        }

        @Override
        public void updateTimeout(String key, long timeout) {
            super.updateTimeout(prefix(key), timeout);
        }

        @Override
        public List<String> searchData(String prefix, String keyword, int start, int size, boolean sortType) {
            return super.searchData(prefix(prefix), keyword, start, size, sortType).stream()
                    .map(key -> key.substring(KEY_PREFIX.length()))
                    .toList();
        }

        private String prefix(String key) {
            return KEY_PREFIX + key;
        }
    }
}
