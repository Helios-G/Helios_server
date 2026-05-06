package com.helios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Helios API 명세서")
                        .description("의료 AI 서비스 Helios의 백엔드 API 문서입니다.")
                        .version("v1.0.0"));
    }
}