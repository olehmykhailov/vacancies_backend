package com.olehmykhailov.vacancies.vacancies.businesslayer.dtos;

import com.olehmykhailov.vacancies.vacancies.datalayer.enums.VacancyStatusEnum;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CreateVacancyRequestDto(
        String title,
        String description,
        String company,

        @NotBlank(message = "ERR:URL_BLANK")
        String url,
        Set<String> stack
) {
}
