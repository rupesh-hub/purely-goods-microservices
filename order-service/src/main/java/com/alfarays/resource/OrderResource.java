package com.alfarays.resource;

import com.alfarays.client.ProductServiceClient;
import com.alfarays.model.GlobalResponse;
import com.alfarays.model.OrderConfirmationDto;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderResource {

    private final ProductServiceClient productService;
    private final StreamBridge streamBridge;

    @GetMapping("/create/{productId}")
    @Retry(name = "productById", fallbackMethod = "fallbackMethod")
    public ResponseEntity<GlobalResponse<?>> productById(@PathVariable("productId") Long productId) {
        var product = productService.get(productId);
        orderConfirmationEvent(
                new OrderConfirmationDto(
                        productId,
                        UUID.randomUUID().toString(),
                        "PENDING",
                        LocalDateTime.now()
                )
        );
        return ResponseEntity.ok(
                GlobalResponse.success("Your order has been placed successfully.", product)
        );
    }


    public ResponseEntity<GlobalResponse<?>> fallbackMethod(Throwable throwable) {
        log.debug(" ********** fallbackMethod() invoked ********** ");
        return new ResponseEntity<>(GlobalResponse.error(throwable.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    private void orderConfirmationEvent(OrderConfirmationDto dto) {
        log.info("Order confirmation event publishing....");
        var result = streamBridge.send("sendCommunication-out-0", dto);
        log.info("Is the Communication request successfully triggered ? : {}", result);
    }

/** @RateLimiter(name= "getJavaVersion", fallbackMethod = "getJavaVersionFallback") */

}
