package com.olehmykhailov.vacancies.users.businesslayer.dtos;

import java.util.UUID;

public record GetUserResponseDto(
        UUID id,
        String email
) {
}
