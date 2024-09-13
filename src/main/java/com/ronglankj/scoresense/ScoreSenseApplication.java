package com.ronglankj.scoresense;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.ronglankj.scoresense.repository")
@SpringBootApplication
public class ScoreSenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoreSenseApplication.class, args);
    }

}
