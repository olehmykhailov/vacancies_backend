package com.olehmykhailov.vacancies.infrastructure.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timestamp,
        String error,
        String message,
        int statusCode,
        String path,

        Map<String, String> validationErrors
) {
}
