package com.alfarays;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
        info = @Info(
                title = "Product Service Microservice API",
                version = "1.0.0",
                summary = "Provides APIs for managing products, categories, specifications, and images",
                description = "REST API Documentation for the Product Service, part of the Alfarays e-commerce microservices architecture.",
                termsOfService = "https://www.alfarays.tech/terms",
                contact = @Contact(
                        name = "Rupesh Dulal",
                        email = "rupeshdulal672@gmail.com",
                        url = "https://www.alfarays.tech"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),

        servers = {
                @Server(
                        description = "Local Development Server",
                        url = "http://localhost:8081"
                ),
                @Server(
                        description = "Staging Server",
                        url = "https://api.alfarays.tech/staging/product-service"
                ),
                @Server(
                        description = "Production Server",
                        url = "https://api.alfarays.tech/product-service"
                )
        },

        tags = {
                @Tag(name = "Products", description = "Product CRUD operations and product metadata management"),
                @Tag(name = "Categories", description = "Manage product categories"),
                @Tag(name = "Images", description = "Image upload, processing and storage operations"),
                @Tag(name = "Health", description = "Service health and monitoring endpoints")
        },

        externalDocs = @ExternalDocumentation(
                description = "Alfarays Technical Documentation",
                url = "https://docs.alfarays.tech"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication using Bearer tokens",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
//@EnableConfigurationProperties(value = {ProductServiceInfoDto.class})
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}