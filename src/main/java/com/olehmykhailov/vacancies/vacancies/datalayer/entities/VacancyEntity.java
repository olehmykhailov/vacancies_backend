package com.olehmykhailov.vacancies.vacancies.datalayer.entities;

import com.olehmykhailov.vacancies.common.BaseEntity;
import com.olehmykhailov.vacancies.users.datalayer.entities.UserEntity;
import com.olehmykhailov.vacancies.vacancies.datalayer.enums.VacancyStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vacancies")
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class VacancyEntity extends BaseEntity {
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "company")
    private String company;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VacancyStatusEnum status = VacancyStatusEnum.SENT;

    @Column(name = "url")
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "vacancy_technology",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private Set<TechnologyEntity> stack = new HashSet<>();
}
