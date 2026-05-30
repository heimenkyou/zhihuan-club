package cn.luowb.clubrecruitment.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class JSONConfiguration {

    // 默认的格式化格式
    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(dateTimeFormat);
            builder.serializers( // 注册Java 8日期序列化器
                    new LocalDateSerializer(dateFormatter),
                    new LocalDateTimeSerializer(dateTimeFormatter)
            );
            // 反序列化器
            builder.deserializers(
                    new LocalDateDeserializer(dateFormatter),
                    new LocalDateTimeDeserializer(dateTimeFormatter)
            );
        };
    }
}