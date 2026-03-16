package com.deco.yakbang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
        		.addServersItem(new Server().url("https://d-jvision.duckdns.org")) // 주소를 https로 고정!
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Yakbang API Documentation")
                .description("약방(Yakbang) 서비스 API 명세서입니다. Copyright. Deco.")
                .version("1.0.0");
    }
}