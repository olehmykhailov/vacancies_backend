package com.olehmykhailov.vacancies.vacancies.businesslayer.controllers;

import com.olehmykhailov.vacancies.vacancies.businesslayer.dtos.*;
import com.olehmykhailov.vacancies.vacancies.businesslayer.services.VacanciesService;
import com.olehmykhailov.vacancies.vacancies.datalayer.enums.VacancyStatusEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/vacancies")
@RequiredArgsConstructor
public class VacanciesController {
    private final VacanciesService vacanciesService;

    @GetMapping("/{userId}")
    public ResponseEntity<Page<GetVacancyResponseDto>> getUserVacancies(
            @PathVariable(name = "userId") UUID userId,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
            ) {
        Page<GetVacancyResponseDto> userVacancies = vacanciesService.getUserVacancies(userId, page, size);
        return ResponseEntity.ok().body(userVacancies);
    }

    @PostMapping
    public ResponseEntity<CreateVacancyResponseDto> createVacancy(
            @Valid @RequestBody CreateVacancyRequestDto createVacancyRequestDto
            ) {
        CreateVacancyResponseDto createdVacancy = vacanciesService.createVacancy(createVacancyRequestDto);
        return ResponseEntity.ok().body(createdVacancy);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PatchVacancyResponseDto> updateStatus(
            @PathVariable(name = "id") UUID id,
            @RequestParam(name = "status") VacancyStatusEnum status
    ) {
        PatchVacancyResponseDto updatedVacancy = vacanciesService.updateStatus(id, status);
        return ResponseEntity.ok().body(updatedVacancy);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PatchVacancyResponseDto> updatedVacancy(
            @PathVariable(name = "id") UUID id,
            @RequestBody PatchVacancyRequestDto patchVacancyRequestDto
    ) {
        PatchVacancyResponseDto updatedVacancy = vacanciesService.updateVacancy(id, patchVacancyRequestDto);
        return ResponseEntity.ok().body(updatedVacancy);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(
            @PathVariable(name = "id") UUID id
    ) {
        vacanciesService.deleteVacancy(id);
        return ResponseEntity.ok().build();
    }
}
