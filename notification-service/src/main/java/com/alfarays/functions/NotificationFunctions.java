package com.alfarays.functions;

import com.alfarays.model.OrderConfirmationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class NotificationFunctions {

    private static final Logger log = LoggerFactory.getLogger(NotificationFunctions.class);

    @Bean
    public Function<OrderConfirmationDto, OrderConfirmationDto> email() {
        return orderConfirmation -> {
            log.info("Sending email with the details : " + orderConfirmation.toString());
            return orderConfirmation;
        };
    }

    @Bean
    public Function<OrderConfirmationDto, String> sms() {
        return orderConfirmation -> {
            log.info("Sending sms with the details : " + orderConfirmation.toString());
            return orderConfirmation.referenceNumber();
        };
    }

}
