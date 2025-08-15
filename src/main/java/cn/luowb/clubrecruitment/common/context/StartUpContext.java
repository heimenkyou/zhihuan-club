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
    public static String hostAddress = "127.0.0.1";

    @Override
    public void run(ApplicationArguments args) {
//        hostAddress = SystemUtil.getHostInfo().getAddress();
        log.info("测试页面:: http://{}:{}{}/test", hostAddress, serverPort, contextPath);
    }

}
