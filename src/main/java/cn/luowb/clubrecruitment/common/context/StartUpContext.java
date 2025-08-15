package cn.luowb.clubrecruitment.common.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartUpContext implements ApplicationRunner {
    @Value("${server.port:8080}")
    private String serverPort;
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("测试页面:: http://127.0.0.1:{}{}/test", serverPort, contextPath);
    }

}
