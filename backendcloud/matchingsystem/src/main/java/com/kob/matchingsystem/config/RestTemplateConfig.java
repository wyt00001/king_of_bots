package com.kob.matchingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    //Spring Boot 里的 Bean 就是“被 Spring 容器接管的对象”——你不再 new，而是让容器帮你创建、组装、销毁，并且随时随地可以“拿来就用”。
}
