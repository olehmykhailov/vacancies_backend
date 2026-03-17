package com.olehmykhailov.vacancies.auth.dtos;

import com.olehmykhailov.vacancies.users.businesslayer.dtos.GetUserResponseDto;


public record SignInResponseDto(
        GetUserResponseDto user,
        String token
) {
}
