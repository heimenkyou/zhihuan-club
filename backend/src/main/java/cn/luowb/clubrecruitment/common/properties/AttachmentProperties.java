package cn.luowb.clubrecruitment.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

/**
 * 附件上传限制配置。
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.attachment")
public class AttachmentProperties {
    private DataSize maxImageSize = DataSize.ofMegabytes(1);
}
