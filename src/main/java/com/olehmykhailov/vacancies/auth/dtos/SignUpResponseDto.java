package com.olehmykhailov.vacancies.auth.dtos;

import java.util.UUID;

public record SignUpResponseDto(
        UUID id,
        String email
) {
}
