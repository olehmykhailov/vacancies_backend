package com.olehmykhailov.vacancies.vacancies.datalayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "technology")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "stack")
    @JsonIgnore
    private Set<VacancyEntity> applications = new HashSet<>();
}
