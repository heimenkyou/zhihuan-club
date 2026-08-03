package cn.luowb.clubrecruitment.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 管理后台登录校验配置。
 */
@Configuration
public class SaTokenConfiguration implements WebMvcConfigurer {

    /**
     * 保护全部后台接口，仅允许登录接口匿名访问。
     *
     * @param registry MVC 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()))
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/auth/login");
    }
}
