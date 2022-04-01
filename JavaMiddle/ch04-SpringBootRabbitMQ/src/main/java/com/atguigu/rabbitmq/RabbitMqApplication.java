package com.atguigu.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
*@author dai
*@create 2022-01-2022/1/6  16-02-13
*/

@SpringBootApplication
@EnableWebMvc
public class RabbitMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApplication.class);
    }
}
