package cn.luowb.clubrecruitment.config;

import cn.hutool.core.util.StrUtil;
import cn.luowb.clubrecruitment.common.context.UserContext;
import cn.luowb.clubrecruitment.common.context.UserInfoDTO;
import cn.luowb.clubrecruitment.common.errorcode.BaseErrorCode;
import cn.luowb.clubrecruitment.common.result.Result;
import cn.luowb.clubrecruitment.common.util.RedisKeyUtil;
import cn.luowb.clubrecruitment.common.web.Results;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 用户相关配置类
 */
@Configuration
@RequiredArgsConstructor
public class UserConfiguration implements WebMvcConfigurer {
    /**
     * 用户信息传递拦截器
     */
    private final UserTransmitInterceptor userTransmitInterceptor;
    /**
     * 权限校验拦截器
     */
    private final AuthInterceptor authInterceptor;

    /**
     * 添加拦截器并设置执行顺序
     * 用户传递拦截器先执行，然后是权限校验拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 用户信息传递拦截器 - 拦截所有请求，尝试解析token但不强制登录
        registry.addInterceptor(userTransmitInterceptor)
                .addPathPatterns("/**")
                .order(1); // 设置优先级，数字越小优先级越高

        // 权限校验拦截器 - 只拦截需要登录的接口
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/public/**", "/test/**")
                .order(2); // 后执行
    }

    /**
     * 用户信息传递拦截器
     * 负责从token解析用户信息并设置到上下文，不强制要求登录
     */
    @Component
    @RequiredArgsConstructor
    static class UserTransmitInterceptor implements HandlerInterceptor {
        private final StringRedisTemplate stringRedisTemplate;
        private final RedisKeyUtil redisKeyUtil;

        @Override
        public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) throws Exception {
            // 请求头获取token
            String token = getToken(request);
            if (StrUtil.isBlank(token)) {
                // token不存在直接放行，不拦截
                return true;
            }

            // 从redis中获取用户信息
            String key = redisKeyUtil.buildAdminTokenKey(token);
            String userInfoStr = stringRedisTemplate.opsForValue().get(key);
            if (userInfoStr == null) {
                // redis中没有用户信息，直接放行，不拦截
                return true;
            }

            UserInfoDTO userInfoDTO;
            try {
                userInfoDTO = JSON.parseObject(userInfoStr, UserInfoDTO.class);
            } catch (JSONException e) {
                // json解析失败，直接放行，不拦截
                return true;
            }
            if (userInfoDTO == null) {
                // 用户信息为空，直接放行，不拦截
                return true;
            }

            userInfoDTO.setToken(token);
            long ttlSeconds = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
            long threshold = Duration.ofMinutes(20).getSeconds();   // 20 分钟以内续期
            if (ttlSeconds < threshold) {
                // 重新设置完整 TTL
                stringRedisTemplate.expire(key, Duration.ofMinutes(30));
            }
            // 设置用户信息到上下文
            UserContext.setUser(userInfoDTO);
            return true;
        }

        private String getToken(HttpServletRequest request) {
            if (request == null) {
                return null;
            }
            String header = request.getHeader("Authorization");
            if (StrUtil.isBlank(header) || !header.startsWith("Bearer ")) {
                return null;
            }
            return header.substring(7);
        }

        @Override
        public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler, Exception exception) throws Exception {
            UserContext.removeUser();
        }
    }

    /**
     * 权限校验拦截器
     * 负责校验用户是否已登录，拦截需要登录的接口
     */
    @Component
    @RequiredArgsConstructor
    static class AuthInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) throws Exception {
            // 检查用户是否已登录
            if (UserContext.getUser() == null) {
                sendError(response, BaseErrorCode.TOKEN_NULL_ERROR);
                return false;
            }
            return true;
        }

        private void sendError(HttpServletResponse response, BaseErrorCode baseErrorCode) {
            if (response == null) {
                return;
            }
            Result<Void> result = Results.failure()
                    .setCode(baseErrorCode.code())
                    .setMessage(baseErrorCode.message());
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JSON.toJSONString(result));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
