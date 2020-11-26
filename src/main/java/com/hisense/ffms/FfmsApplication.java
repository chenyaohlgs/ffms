package com.hisense.ffms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "com.hisense.ffms.dao")
@SpringBootApplication
public class FfmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FfmsApplication.class, args);
    }

}
