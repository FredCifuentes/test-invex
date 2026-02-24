package com.invex.testinvex.employee.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Service API")
                        .description("REST API for employee management (technical assessment)")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Daniel Cifuentes")
                                .email("fredy97daniel@gmail.com"))
                        .license(new License()
                                .name("Internal Use")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project documentation")
                        .url("http://localhost:8080/swagger-ui.html"));
    }
}
