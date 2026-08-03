package cn.luowb.clubrecruitment.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云对象存储配置。
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.qiniu")
public class QiniuProperties {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domain;
}
