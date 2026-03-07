package com.olehmykhailov.vacancies.users.datalayer.entities;

import com.olehmykhailov.vacancies.common.BaseEntity;
import com.olehmykhailov.vacancies.vacancies.datalayer.entities.VacancyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<VacancyEntity> vacancies;
}
