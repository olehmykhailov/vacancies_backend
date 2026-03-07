package com.olehmykhailov.vacancies.users.businesslayer.dtos;

import java.util.UUID;

public record UpdateUserResponseDto(
        UUID id,
        String email
) {
}
