package com.olehmykhailov.vacancies.vacancies.businesslayer.dtos;

import com.olehmykhailov.vacancies.vacancies.datalayer.enums.VacancyStatusEnum;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record PatchVacancyResponseDto(
        UUID id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String title,
        String description,
        String company,
        VacancyStatusEnum status,
        String url,
        Set<String> stack
) {
}
