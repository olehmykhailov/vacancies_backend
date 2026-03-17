package com.olehmykhailov.vacancies.vacancies.businesslayer.dtos;

import java.util.Set;

public record PatchVacancyRequestDto(
        String title,
        String description,
        String company,
        String url,
        Set<String> stack
) {
}
