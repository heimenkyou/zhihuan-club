package cn.luowb.clubrecruitment.config;

import cn.luowb.clubrecruitment.common.context.IPContext;
import cn.luowb.clubrecruitment.common.context.VisitorTokenContext;
import cn.luowb.clubrecruitment.common.util.VisitorTokenUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {
    private final IPInterceptor ipInterceptor;
    private final VisitorTokenInterceptor visitorTokenInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(ipInterceptor)
                .addPathPatterns("/**")
                .order(0); // IP拦截器最先执行，设置为最高优先级
        registry.addInterceptor(visitorTokenInterceptor)
                .addPathPatterns("/**")
                .order(1); // 访客令牌拦截器，保证后续业务能读取当前访客标识
    }

    @Component
    static class IPInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                 @NonNull Object handler) {
            String clientIP = JakartaServletUtil.getClientIP(request);
            IPContext.setIp(clientIP);
            return true;
        }

        @Override
        public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull Object handler, @Nullable Exception ex) {
            IPContext.removeIp();
        }
    }

    @Component
    static class VisitorTokenInterceptor implements HandlerInterceptor {
        @Value("${app.visitor-token.cookie-secure:true}")
        private boolean cookieSecure;

        @Override
        public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                 @NonNull Object handler) {
            String token = getCookieValue(request);
            if (token == null) {
                token = VisitorTokenUtil.generateToken();
                addVisitorTokenCookie(response, token);
            }
            VisitorTokenContext.setToken(token);
            return true;
        }

        @Override
        public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull Object handler, @Nullable Exception ex) {
            VisitorTokenContext.removeToken();
        }

        private String getCookieValue(HttpServletRequest request) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return null;
            }
            for (Cookie cookie : cookies) {
                if (VisitorTokenContext.COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
            return null;
        }

        private void addVisitorTokenCookie(HttpServletResponse response, String token) {
            String secure = cookieSecure ? "Secure; " : "";
            response.addHeader("Set-Cookie",
                    VisitorTokenContext.COOKIE_NAME + "=" + token
                            + "; Max-Age=" + VisitorTokenContext.COOKIE_MAX_AGE_SECONDS
                            + "; Path=/; HttpOnly; " + secure + "SameSite=Lax");
        }
    }
}
