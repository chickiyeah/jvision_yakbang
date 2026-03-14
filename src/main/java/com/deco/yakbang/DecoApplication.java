package com.deco.yakbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan(basePackages = "com.deco.yakbang.service.impl") // Mapper 인터페이스가 있는 패키지 경로를 적으세요
@SpringBootApplication(exclude = { org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })
public class DecoApplication extends SpringBootServletInitializer {
	// 2. 외장 톰캣 배포를 위한 설정 메서드 추가
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DecoApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(DecoApplication.class, args);
    }
}
