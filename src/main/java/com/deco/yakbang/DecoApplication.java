package com.deco.yakbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//SecurityAutoConfiguration을 제외하면 리다이렉트가 사라집니다.
@SpringBootApplication(exclude = { org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })
public class DecoApplication {
 public static void main(String[] args) {
     SpringApplication.run(DecoApplication.class, args);
 }
}
