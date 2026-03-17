package com.olehmykhailov.vacancies.infrastructure.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest req) {
        return buildResponse(
                "ERR:NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                req.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex, HttpServletRequest req) {
        return buildResponse(
                "ERR:CONFLICT",
                ex.getMessage(),
                HttpStatus.CONFLICT,
                req.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(
                (error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                }
        );

        return buildResponse(
                "ERR:VALIDATION",
                "ERR:VALIDATION",
                HttpStatus.BAD_REQUEST,
                req.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex, HttpServletRequest req) {
        return buildResponse(
                "ERR:UNAUTHORIZED",
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                req.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidPasswordException ex, HttpServletRequest req) {
        return buildResponse(
                "ERR:UNAUTHORIZED",
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                req.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest req) {
        log.error("Internal Server error at {}", req.getRequestURI(), ex);
        return buildResponse(
                "ERR:INTERNAL_SERVER_ERROR",
                "ERR:INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                req.getRequestURI(),
                null
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            String error, String message, HttpStatus status, String path, Map<String, String> errors) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                error,
                message,
                status.value(),
                path,
                errors
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}
