package com.alfarays.functions;

import com.alfarays.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class OrderFunctions {

    private static final Logger log = LoggerFactory.getLogger(OrderFunctions.class);

    @Bean
    public Consumer<String> updateCommunication(OrderService orderService) {
        return reference -> {
            log.info("Updating Communication status for the order: " + reference);
            //TODO: perform business logic
            //orderService.updateOrderStatus(reference);
        };
    }
}
