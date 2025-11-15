package com.localhelper.config;

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

@Configuration
public class SwaggerConfig {
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(getApiInfo())
                .servers(getServers())
                .components(getComponents())
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
    
    private Info getApiInfo() {
        return new Info()
                .title("Local Helper Backend API")
                .description("REST API for Local Helper Community Platform - A comprehensive service marketplace connecting users with local service providers")
                .version("1.0.0")
                .contact(getContact())
                .license(getLicense());
    }
    
    private Contact getContact() {
        return new Contact()
                .name("Local Helper Team")
                .email("support@localhelper.com")
                .url("https://localhelper.com");
    }
    
    private License getLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }
    
    private List<Server> getServers() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server");
        
        Server prodServer = new Server()
                .url("https://api.localhelper.com")
                .description("Production Server");
        
        return List.of(localServer, prodServer);
    }
    
    private Components getComponents() {
        return new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT token for authentication. Format: Bearer {token}"));
    }
}