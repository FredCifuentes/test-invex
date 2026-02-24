package com.invex.testinvex.employee.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ApiErrorResponse", description = "Standard API error response")
public class ApiErrorResponse {
    @Schema(example = "2026-02-23T14:06:41")
    private LocalDateTime timestamp;

    @Schema(example = "400")
    private int status;

    @Schema(example = "Bad Request")
    private String error;

    @Schema(example = "Validation failed")
    private String message;

    @Schema(example = "/api/employees")
    private String path;

    @Schema(description = "Validation error details")
    private List<String> details;
}
