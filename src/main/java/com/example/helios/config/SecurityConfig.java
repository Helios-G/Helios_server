package com.example.helios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 설정 활성화
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. CSRF 비활성화 (REST API 환경에서 POST 테스트를 위해 필수)
            .csrf(csrf -> csrf.disable()) 
            
            // 2. 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 세션 관련 API는 모두 허용
                .requestMatchers("/sessions/**").permitAll() 
                
                // 나머지 요청(member, learning 등)도 일단은 테스트를 위해 허용
                // 만약 나중에 인증이 필요해지면 .authenticated()로 바꾸면 됩니다.
                .anyRequest().permitAll() 
            );

        return http.build();
    }
}