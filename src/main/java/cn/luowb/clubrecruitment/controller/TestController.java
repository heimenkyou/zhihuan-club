package cn.luowb.clubrecruitment.controller;

import cn.hutool.core.io.FileUtil;
import cn.luowb.clubrecruitment.common.util.IpUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/test")
@Tag(name = "测试")
public class TestController {

    @GetMapping()
    public String test(HttpServletRequest request) {
        // 变量
        String clientIP = IpUtil.getClientIP(request);
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String userAgent = request.getHeader("User-Agent");

        // 读取 HTML 文件
        String html = FileUtil.readString("static/test.html", StandardCharsets.UTF_8);
        // 替换占位符
        html = html.replace("{{currentTime}}", currentTime)
                .replace("{{clientIP}}", clientIP)
                .replace("{{userAgent}}", userAgent);

        return html;
    }

}
