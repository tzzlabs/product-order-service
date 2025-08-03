package com.example.productorderservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "basicAuth";

    @Bean
    public OpenAPI productOrderServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Product Order Service API")
                .description("CRUD APIs for managing product orders")
                .version("1.0")
                .contact(new Contact().name("Your Team").email("support@example.com")))
            .components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic")))
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}
