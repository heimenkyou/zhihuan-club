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
     * 用户信息传输拦截器
     */
    private final UserTransmitInterceptor userTransmitInterceptor;

    /**
     * 添加用户信息传递过滤器至相关路径拦截
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userTransmitInterceptor)
                .addPathPatterns("/**", "/admin/**")
                .excludePathPatterns("/public/**", "/test/**");
    }

    /**
     * 用户信息传输拦截器
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
                sendError(response, BaseErrorCode.TOKEN_NULL_ERROR);
                return false;
            }
            // 从redis中获取用户信息
            String key = redisKeyUtil.buildAdminTokenKey(token);
            String userInfoStr = stringRedisTemplate.opsForValue().get(key);
            if (userInfoStr == null) {
                // redis中没有用户信息
                sendError(response, BaseErrorCode.TOKEN_VERIFY_ERROR);
                return false;
            }
            UserInfoDTO userInfoDTO;
            try {
                userInfoDTO = JSON.parseObject(userInfoStr, UserInfoDTO.class);
            } catch (JSONException e) {
                // json解析失败
                sendError(response, BaseErrorCode.TOKEN_VERIFY_ERROR);
                return false;
            }
            if (userInfoDTO == null) {
                // 用户信息为空
                sendError(response, BaseErrorCode.TOKEN_VERIFY_ERROR);
                return false;
            }
            userInfoDTO.setToken(token);
            long ttlSeconds = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
            long threshold = Duration.ofMinutes(20).getSeconds();   // 20 分钟以内续期
            if (ttlSeconds < threshold) {
                // 重新设置完整 TTL
                stringRedisTemplate.expire(key, Duration.ofMinutes(30));
            }
            // 登录成功，设置用户信息
            UserContext.setUser(userInfoDTO);
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
}
