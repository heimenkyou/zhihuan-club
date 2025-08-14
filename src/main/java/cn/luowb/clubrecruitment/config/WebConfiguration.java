package cn.luowb.clubrecruitment.config;

import cn.luowb.clubrecruitment.common.context.IPContext;
import cn.luowb.clubrecruitment.common.util.IpUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {
    private final IPInterceptor ipInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipInterceptor)
                .addPathPatterns("/**");
    }

    @Component
    static class IPInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) throws Exception {
            String clientIP = IpUtil.getClientIP(request);
            IPContext.setIp(clientIP);
            return true;
        }

        @Override
        public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler, Exception ex) throws Exception {
            IPContext.removeIp();
        }
    }
}
