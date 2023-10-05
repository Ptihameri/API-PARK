package com.example.demoudemyapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenaApiConfig {
    //TODO https://www.baeldung.com/spring-rest-openapi-documentation
    @Bean
    public OpenAPI openaApiConfig() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Authorization", securityScheme()))
                .info(
                        new Info()
                                .title("Rest API - Spring")
                                .description("API para gestao de estacionamento")
                                .version("v1")
                                .license(new License().name("Apache 2.0").url("http://apache.org/licenses/LICENSE-2.0"))
                                .contact(new Contact().name("tihas").email("Drogaéotihas@tihameri.com"))
                );
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .description("Insira o token para acessar este serviço")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization");
    }
}
