package com.olehmykhailov.vacancies.auth.dtos;

import jakarta.validation.constraints.NotBlank;

public record SignInRequestDto(
        @NotBlank(message = "ERR:EMAIL_BLANK")
        String email,

        @NotBlank(message = "ERR:PASSWORD_BLANK")
        String password
) {
}
