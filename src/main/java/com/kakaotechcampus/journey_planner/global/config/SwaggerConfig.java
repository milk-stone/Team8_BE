package com.kakaotechcampus.journey_planner.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // JWT 인증 스키마 정의
        SecurityScheme jwtAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("JWT 토큰을 입력하세요. 예시: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("JWT", jwtAuthScheme))
                .addSecurityItem(new SecurityRequirement().addList("JWT")) // 전역 적용
                .info(apiInfo())
                .servers(List.of(
                        new Server().url("https://api.journey-planner.org:8080").description("Production HTTPS 서버"),
                        new Server().url("http://localhost:8080").description("로컬 개발 서버")
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("Journey Planner API")
                .description("카카오테크캠퍼스 3기 경북대 4팀의 백엔드 API 명세")
                .version("1.0.0");
    }
}