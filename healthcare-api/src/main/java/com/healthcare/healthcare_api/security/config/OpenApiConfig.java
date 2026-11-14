package com.healthcare.healthcare_api.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI healthcareApiOpenApi() {

        final String securityScheme = "Bearer Authentication";

        return new OpenAPI().info(new Info().title("Healthcare Core APIs").description("""
                                Core service for Healthcare Management System.
                                
                                Features:
                                • --
                                • --
                                • --
                                • --
                                • --
                                • --
                                • --
                                • --
                                """)
                        .version("1.0.0")
                        .contact(new Contact().name("Negar").email("negar.oskouie@gmail.com")))
                .components(new Components().addSecuritySchemes(securityScheme,
                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer").bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(securityScheme)

                );
    }
}
