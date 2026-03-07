package com.olehmykhailov.vacancies.users.businesslayer.dtos;

import java.util.UUID;

public record CreateUserResponseDto(
        UUID id,
        String email
) {
}
