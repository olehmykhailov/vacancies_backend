package com.olehmykhailov.vacancies.users.businesslayer.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserEmailRequestDto(
        @NotBlank(message = "ERR:EMAIL_BLANK")
        @Email(message = "ERR:NOT_EMAIL")
        String email
) {
}
