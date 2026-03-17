package com.olehmykhailov.vacancies.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequestDto(
        @NotBlank(message = "ERR:EMAIL_BLANK")
        @Email(message = "ERR:INVALID_EMAIL")
        String email,

        @NotBlank(message = "ERR:PASSWORD_BLANK")
        @Size(min = 8, max = 32, message = "ERR:PASSWORD_SIZE")
        @Pattern(
                regexp = "\"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d).{8,}$\"",
                message = "ERR:PASSWORD_LETTERS"
        )
        String password
) {
}
