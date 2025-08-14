package cn.luowb.clubrecruitment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.luowb.clubrecruitment.dao.mapper")
@SpringBootApplication
public class ClubRecruitmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubRecruitmentApplication.class, args);
    }

}
