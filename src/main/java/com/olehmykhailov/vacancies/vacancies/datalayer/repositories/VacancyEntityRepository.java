package com.olehmykhailov.vacancies.vacancies.datalayer.repositories;

import com.olehmykhailov.vacancies.vacancies.datalayer.entities.VacancyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VacancyEntityRepository extends JpaRepository<VacancyEntity, UUID> {
    Page<VacancyEntity> findVacanciesByUserId(UUID userId, Pageable pageable);
}
