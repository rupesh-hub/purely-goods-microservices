package com.alfarays.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class CustomSecurityConfiguration {
    
    @Value("${application.cors.origins}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeExchange(
                        exchanges -> exchanges
                                .pathMatchers("/payment-service/**").permitAll()
                                .pathMatchers("/product-service/**").hasRole("PRODUCTS")
                                .pathMatchers("/order-service/**").hasRole("ORDERS")
                                .pathMatchers("/payment-service/**").hasRole("PAYMENTS")
                                .pathMatchers("/cart-service/**").hasRole("CARTS"))
                .oauth2ResourceServer(
                        spec -> spec.jwt(
                                jwt -> jwt.jwtAuthenticationConverter(
                                        grantedAuthorityExtractor()
                                )
                        )
                );
        httpSecurity.csrf(
                ServerHttpSecurity.CsrfSpec::disable
        );

        httpSecurity.cors(
                config -> config.configurationSource(
                        request -> {

                            final CorsConfiguration configuration = new CorsConfiguration();
                            configuration.setAllowCredentials(true);
                            configuration.setAllowedOrigins(allowedOrigins);
                            configuration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
                            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH"));
                            return configuration;
                        }
                )
        );

        return httpSecurity.build();

    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthorityExtractor() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

}
