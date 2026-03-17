package com.olehmykhailov.vacancies.vacancies.businesslayer.mappers;

import com.olehmykhailov.vacancies.vacancies.businesslayer.dtos.*;
import com.olehmykhailov.vacancies.vacancies.datalayer.entities.VacancyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VacanciesMapper {
    GetVacancyResponseDto toGetResponseDtoFromEntity(VacancyEntity entity);

    @Mapping(target = "status", constant = "SENT")
    VacancyEntity toEntityFromCreateRequestDto(CreateVacancyRequestDto createVacancyRequestDto);

    CreateVacancyResponseDto toCreateResponseDtoFromEntity(VacancyEntity vacancyEntity);

    PatchVacancyResponseDto toPatchResponseDtoFromEntity(VacancyEntity vacancyEntity);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(PatchVacancyRequestDto dto, @MappingTarget VacancyEntity entity);

    default String map(com.olehmykhailov.vacancies.vacancies.datalayer.entities.TechnologyEntity entity) {
        return entity.getName();
    }

    default com.olehmykhailov.vacancies.vacancies.datalayer.entities.TechnologyEntity map(String name) {
        com.olehmykhailov.vacancies.vacancies.datalayer.entities.TechnologyEntity entity = new com.olehmykhailov.vacancies.vacancies.datalayer.entities.TechnologyEntity();
        entity.setName(name);
        return entity;
    }
}
