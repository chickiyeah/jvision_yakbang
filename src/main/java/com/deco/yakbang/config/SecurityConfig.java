package com.deco.yakbang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.deco.yakbang.security.JwtAuthenticationFilter;
import com.deco.yakbang.security.JwtTokenProvider;

import jakarta.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Resource
    private JwtTokenProvider jwtTokenProvider;
	//here!
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(basic -> basic.disable())
            .csrf(csrf -> csrf.disable()) // REST API이므로 csrf 비활성화
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 미사용
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/","/error", "/index.html", "/favicon.ico", "/static/**", "/css/**", "/js/**").permitAll()
                .requestMatchers("/yakbang/api/user/login", "/yakbang/api/user/register", "/yakbang/api/user/check-id.do", "/yakbang/api/user/salt/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll() // Swagger 허용
                .requestMatchers("/yakbang/api/app/**").authenticated() // 이 줄이 추가되어야 합니다.
                .anyRequest().authenticated() // 그 외 profile, delete 등은 인증 필요
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), 
                    UsernamePasswordAuthenticationFilter.class);
        
        
        
        return http.build();
    }
}