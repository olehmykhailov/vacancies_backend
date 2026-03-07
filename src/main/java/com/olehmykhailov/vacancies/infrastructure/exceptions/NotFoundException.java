package com.olehmykhailov.vacancies.infrastructure.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
