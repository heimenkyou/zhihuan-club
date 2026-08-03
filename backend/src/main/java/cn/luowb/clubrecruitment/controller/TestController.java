package cn.luowb.clubrecruitment.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.system.RuntimeInfo;
import cn.hutool.system.SystemUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "测试")
@Profile({"dev", "test"})
public class TestController {
    // 异常页面字面量

    private static final String ERROR_HTML = """
            <!DOCTYPE html>
            <html lang="zh-CN">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>出错了</title>
                <style>
                    body {
                        font-family: 'Helvetica', 'Arial', sans-serif;
                        background-color: #f2f2f2;
                        color: #333;
                        display: flex;
                        flex-direction: column;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        margin: 0;
                    }
                    .container {
                        background-color: #fff;
                        padding: 30px 40px;
                        border-radius: 8px;
                        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                        text-align: center;
                        max-width: 500px;
                        width: 100%;
                    }
                    h1 {
                        font-size: 2em;
                        margin-bottom: 15px;
                        color: #e74c3c;
                    }
                    p {
                        margin: 5px 0;
                        font-size: 1em;
                    }
                    .info {
                        margin-top: 20px;
                        font-size: 0.85em;
                        color: #999;
                        word-break: break-all;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>哎呀，出现错误了！</h1>
                    <p>请稍后再试或联系管理员。</p>
                    <div class="info">
                        时间: {{currentTime}}<br>
                        IP: {{clientIP}}<br>
                        User-Agent: {{userAgent}}
                    </div>
                </div>
            </body>
            </html>
            """;
    private String html;

    {
        String path = "static/test.html";
        ClassPathResource resource;
        try {
            resource = new ClassPathResource(path);
            html = resource.readUtf8Str();
        } catch (Exception e) {
            log.error("无法读取文件: {}", path, e);
            html = ERROR_HTML;
        }
    }

    @GetMapping(value = {"/test", "/"})
    public String test(HttpServletRequest request) {
        // 变量
        String clientIP = JakartaServletUtil.getClientIP(request);
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String userAgent = request.getHeader("User-Agent");
        // 系统信息
        RuntimeInfo runtimeInfo = SystemUtil.getRuntimeInfo();
        String maxMemory = FileUtil.readableFileSize(runtimeInfo.getMaxMemory());
        String totalMemory = FileUtil.readableFileSize(runtimeInfo.getTotalMemory());
        String freeMemory = FileUtil.readableFileSize(runtimeInfo.getFreeMemory());
        String usableMemory = FileUtil.readableFileSize(runtimeInfo.getUsableMemory());
        // 替换占位符
        return html.replace("{{currentTime}}", currentTime)
                .replace("{{clientIP}}", clientIP)
                .replace("{{userAgent}}", userAgent)
                .replace("{{maxMemory}}", maxMemory)
                .replace("{{totalMemory}}", totalMemory)
                .replace("{{freeMemory}}", freeMemory)
                .replace("{{usableMemory}}", usableMemory);
    }
}
