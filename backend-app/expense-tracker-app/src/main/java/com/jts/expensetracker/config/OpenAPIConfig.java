package com.jts.expensetracker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Expense Tracker API",
                version = "1.0",
                description = "API documentation for the Expense Tracker application"
        )
)
public class OpenAPIConfig {
        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                        .info(new io.swagger.v3.oas.models.info.Info()
                                .title("Expense Tracker API")
                                .version("1.0")
                                .description("API documentation for Expense Tracker App"))
                        .components(new Components()
                                .addSecuritySchemes("BearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")))
                        .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
        }
}
