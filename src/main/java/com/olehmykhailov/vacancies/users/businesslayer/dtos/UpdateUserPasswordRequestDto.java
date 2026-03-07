package com.olehmykhailov.vacancies.users.businesslayer.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserPasswordRequestDto(
        @NotBlank
        String oldPassword,

        @NotBlank(message = "ERR:PASSWORD_BLANK")
        @Size(min = 8, max = 32, message = "ERR:PASSWORD_SIZE")
        @Pattern(
                regexp = "\"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d).{8,}$\"",
                message = "ERR:PASSWORD_LETTERS"
        )
        String newPassword
) {
}
