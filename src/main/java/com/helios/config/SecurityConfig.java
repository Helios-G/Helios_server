package com.helios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.helios.auth.jwt.JwtAuthenticationEntryPoint;
import com.helios.auth.jwt.JwtAuthenticationFilter;
import com.helios.auth.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // 비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // JWT 필터 Bean 등록
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // CORS 활성화
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // CSRF 비활성화 (JWT 방식에서는 사용 안함)
            .csrf(csrf -> csrf.disable())

            // 기본 로그인 방식 비활성화
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // 세션 사용 안함 (JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                // Preflight 요청 허용 (CORS 핵심)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // 인증 없이 접근 가능
                .requestMatchers("/auth/login", "/auth/signup").permitAll()
                
                // 나머지는 토큰 필요
                .anyRequest().authenticated()
            )

            // 인증 실패 시 처리 (401 응답 커스터마이징)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )

            // JWT 필터 등록
            .addFilterBefore(
                    jwtAuthenticationFilter(),
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOriginPattern("*");

        // 모든 헤더 허용
        config.addAllowedHeader("*");

        // 모든 HTTP 메소드 허용
        config.addAllowedMethod("*");

        // 인증정보 허용
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}