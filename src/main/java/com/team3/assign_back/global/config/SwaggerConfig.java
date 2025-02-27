package com.team3.assign_back.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("오늘 뭐 먹지? Swagger")
                .description("오늘 뭐 먹지? 유저, 추천, 피드, 프로필, 통계에 관한 REST API")
                .version("1.0");
    }
}