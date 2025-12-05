package com.alfarays.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@Schema(
        name = "GlobalResponse",
        description = "Unified generic response wrapper for all API responses."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalResponse<T> {

    @Schema(
            description = "Indicates whether the request was successful",
            example = "true"
    )
    private boolean success;

    @Schema(
            description = "Short response message summarizing the result",
            example = "Product created successfully"
    )
    private String message;

    @Schema(
            description = "Actual response data. Could be an object, list, page, or null.",
            example = "{}",
            nullable = true
    )
    private T data;

    @Schema(
            description = "Additional metadata such as pagination info, warnings, etc.",
            nullable = true,
            example = "{\"page\": 1, \"size\": 10}"
    )
    private Map<String, Object> meta;

    @Schema(
            description = "Timestamp of the response in UTC",
            example = "2025-01-15T10:15:30Z"
    )
    private Instant timestamp;

    // -------- STATIC FACTORY HELPERS --------

    public static <T> GlobalResponse<T> success(String message, T data) {
        return GlobalResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> GlobalResponse<T> success(String message, T data, Map<String, Object> meta) {
        return GlobalResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .meta(meta)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> GlobalResponse<T> error(String message) {
        return GlobalResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> GlobalResponse<T> error(String message, Map<String, Object> meta) {
        return GlobalResponse.<T>builder()
                .success(false)
                .message(message)
                .meta(meta)
                .timestamp(Instant.now())
                .build();
    }
}
