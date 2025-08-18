package com.rca.mis.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI configuration for API documentation
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers())
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private Info apiInfo() {
        return new Info()
                .title("RCA MIS API")
                .description("""
                    Rwanda Coding Academy Management Information System (RCA MIS) API
                    
                    This API provides comprehensive management capabilities for:
                    - User Management & Authentication
                    - Student Management
                    - Teacher Management
                    - Academic Management (Classes, Subjects, Assessments)
                    - Attendance Management
                    - Timetable Management
                    - Reports & Analytics
                    - Communication & Notifications
                    
                    ## Authentication
                    The API uses JWT (JSON Web Token) authentication. Include the token in the Authorization header:
                    ```
                    Authorization: Bearer <your-jwt-token>
                    ```
                    
                    ## Rate Limiting
                    API requests are limited to 100 requests per hour per user.
                    
                    ## Error Handling
                    The API returns appropriate HTTP status codes and detailed error messages.
                    """)
                .version("1.0.0")
                .contact(new Contact()
                        .name("RCA MIS Development Team")
                        .email("dev@rca.ac.rw")
                        .url("https://www.rca.ac.rw"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    private List<Server> servers() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Local Development Server"),
                new Server()
                        .url("https://api.rca.ac.rw")
                        .description("Production Server"),
                new Server()
                        .url("https://staging-api.rca.ac.rw")
                        .description("Staging Server")
        );
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer")
                .description("Enter JWT token in the format: Bearer <token>");
    }
}
