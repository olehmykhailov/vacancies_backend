package com.olehmykhailov.vacancies.vacancies.businesslayer.services;

import com.olehmykhailov.vacancies.infrastructure.exceptions.NotFoundException;
import com.olehmykhailov.vacancies.users.datalayer.repositories.UserEntityRepository;
import com.olehmykhailov.vacancies.vacancies.businesslayer.dtos.*;
import com.olehmykhailov.vacancies.vacancies.businesslayer.mappers.VacanciesMapper;
import com.olehmykhailov.vacancies.vacancies.datalayer.entities.VacancyEntity;
import com.olehmykhailov.vacancies.vacancies.datalayer.enums.VacancyStatusEnum;
import com.olehmykhailov.vacancies.vacancies.datalayer.repositories.VacancyEntityRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VacanciesService {
    private final VacancyEntityRepository vacancyEntityRepository;
    private final VacanciesMapper vacanciesMapper;
    private final UserEntityRepository userEntityRepository;

    public Page<GetVacancyResponseDto> getUserVacancies(
            UUID userId, int page, int size) {
        if (!userEntityRepository.existsById(userId)) {
            throw new NotFoundException("ERR:USER_NOT_FOUND");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());

        Page<VacancyEntity> vacanciesPage = vacancyEntityRepository.findVacanciesByUserId(userId, pageable);

        return vacanciesPage.map(
                vacanciesMapper::toGetResponseDtoFromEntity
        );
    }

    @Transactional
    public CreateVacancyResponseDto createVacancy(
            CreateVacancyRequestDto createVacancyRequestDto
    ) {
        VacancyEntity newVacancy = vacanciesMapper.toEntityFromCreateRequestDto(createVacancyRequestDto);

        VacancyEntity savedVacancy = vacancyEntityRepository.save(newVacancy);

        return vacanciesMapper.toCreateResponseDtoFromEntity(savedVacancy);
    }

    @Transactional
    public PatchVacancyResponseDto updateStatus(
            UUID id, VacancyStatusEnum newStatus
    ) {
        VacancyEntity existVacancy = vacancyEntityRepository.findVacancyById(id)
                .orElseThrow(() -> new NotFoundException("ERR:VACANCY_NOT_FOUND"));

        existVacancy.setStatus(newStatus);

        VacancyEntity updatedVacancy = vacancyEntityRepository.save(existVacancy);

        return vacanciesMapper.toPatchResponseDtoFromEntity(updatedVacancy);
    }

    @Transactional
    public PatchVacancyResponseDto updateVacancy(
        UUID id, PatchVacancyRequestDto patchVacancyRequestDto
    ) {
        VacancyEntity existVacancy = vacancyEntityRepository.findVacancyById(id)
                .orElseThrow(() -> new NotFoundException("ERR:VACANCY_NOT_FOUND"));

        vacanciesMapper.updateEntityFromDto(patchVacancyRequestDto, existVacancy);

        VacancyEntity updatedVacancy = vacancyEntityRepository.save(existVacancy);

        return vacanciesMapper.toPatchResponseDtoFromEntity(updatedVacancy);
    }

    @Transactional
    public void deleteVacancy(
            UUID id
    ) {
        VacancyEntity existVacancy = vacancyEntityRepository.findVacancyById(id)
                .orElseThrow(() -> new NotFoundException("ERR:VACANCY_NOT_FOUND"));

        vacancyEntityRepository.delete(existVacancy);
    }

}
