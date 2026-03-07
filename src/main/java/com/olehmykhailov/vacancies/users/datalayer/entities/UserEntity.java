package com.olehmykhailov.vacancies.users.datalayer.entities;

import com.olehmykhailov.vacancies.common.BaseEntity;
import com.olehmykhailov.vacancies.vacancies.datalayer.entities.VacancyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@SoftDelete(columnName = "deleted_at")
@Setter @Getter
public class UserEntity extends BaseEntity {
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt = null;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<VacancyEntity> vacancies = new ArrayList<>();
}
