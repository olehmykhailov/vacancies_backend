package com.olehmykhailov.vacancies.users.datalayer.repositories;

import com.olehmykhailov.vacancies.users.datalayer.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {
}
