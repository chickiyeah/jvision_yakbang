package com.deco.yakbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.deco.yakbang.service.impl") // Mapper 인터페이스가 있는 패키지 경로를 적으세요
@SpringBootApplication(exclude = { org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })
public class DecoApplication {
 public static void main(String[] args) {
     SpringApplication.run(DecoApplication.class, args);
 }
}
