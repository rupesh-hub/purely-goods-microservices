package com.alfarays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @Bean
    public RouteLocator routeConfig(RouteLocatorBuilder builder) {
        return builder.routes()

                // PRODUCT SERVICE
                /** .route(p -> p
                 .path("/product-service/**")
                 .filters(f -> f
                 .rewritePath("/product-service/(?<segment>.*)", "/api/v1.0.0/products/${segment}")
                 .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                 .uri("lb://PRODUCT-SERVICE"))

                 // ----------------- PRODUCTS -----------------
                 .route("product_route", p -> p
                 .path("/product-service/products/**")
                 .filters(f -> f
                 .rewritePath("/product-service/products/(?<segment>.*)",
                 "/api/v1.0.0/products/${segment}")
                 .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                 .uri("lb://PRODUCT-SERVICE"))

                 // ----------------- CATEGORIES -----------------
                 .route("category_route", p -> p
                 .path("/product-service/categories/**")
                 .filters(f -> f
                 .rewritePath("/product-service/categories/(?<segment>.*)",
                 "/api/v1.0.0/categories/${segment}")
                 .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                 .uri("lb://PRODUCT-SERVICE"))

                 */

                .route(p -> p
                        .path("/product-service/{entity:products|categories}/**")
                        .filters(f -> f
                                .rewritePath("/product-service/(?<entity>products|categories)/?(?<segment>.*)",
                                        "/api/v1.0.0/${entity}/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://PRODUCT-SERVICE"))

                // ORDER SERVICE
                .route(p -> p
                        .path("/order-service/**")
                        .filters(f -> f
                                .rewritePath("/order-service/?(?<segment>.*)",
                                        "/orders/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://ORDER-SERVICE"))


                // CART SERVICE
                .route(p -> p
                        .path("/cart-service/**")
                        .filters(f -> f
                                .rewritePath("/cart-service/(?<segment>.*)", "/carts/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://CART-SERVICE"))

                // PAYMENT SERVICE
                .route(p -> p
                        .path("/payment-service/**")
                        .filters(f -> f
                                .rewritePath("/payment-service/(?<segment>.*)", "/payments/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://PAYMENT-SERVICE"))

                .build();
    }


    /**
     Gateway receives → /product-service/list
     Gateway rewrites → /api/v1.0.0/products/list
     Gateway picks Eureka instance → http://localhost:8081
     Gateway sends → http://localhost:8081/api/v1.0.0/products/list
     */


}
