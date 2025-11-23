package com.alfarays.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
@Builder
public class ErrorResponse {

    @Schema(description = "API path invoked by client")
    private String path;

    @Schema(description = "Error code representing the error happened")
    private HttpStatus code;

    @Schema(description = "Error message representing the error happened")
    private String message;

    @Schema(description = "Time representing when the error happened")
    private LocalDateTime timestamp;

}
