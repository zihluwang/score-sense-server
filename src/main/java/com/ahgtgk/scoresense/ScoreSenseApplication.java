package com.ahgtgk.scoresense;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 公途公考估分助手服务端启动器。
 *
 * @author zihluwang
 */
@MapperScan(basePackages = "com.ahgtgk.scoresense.repository")
@SpringBootApplication
public class ScoreSenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoreSenseApplication.class, args);
    }

}
