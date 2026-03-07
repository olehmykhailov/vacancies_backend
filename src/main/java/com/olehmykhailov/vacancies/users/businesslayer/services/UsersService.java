package com.olehmykhailov.vacancies.users.businesslayer.services;

import com.olehmykhailov.vacancies.infrastructure.exceptions.ConflictException;
import com.olehmykhailov.vacancies.infrastructure.exceptions.NotFoundException;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.CreateUserRequestDto;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.CreateUserResponseDto;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.UpdateUserResponseDto;
import com.olehmykhailov.vacancies.users.businesslayer.mappers.UserMapper;
import com.olehmykhailov.vacancies.users.datalayer.entities.UserEntity;
import com.olehmykhailov.vacancies.users.datalayer.repositories.UserEntityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserEntityRepository userEntityRepository;
    private final UserMapper userMapper;

    private UserEntity findEntityById(UUID id) {
        return userEntityRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("ERR:USER_NOT_FOUND"));
    }

    @Transactional
    public CreateUserResponseDto createUser(
            @Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        if (userEntityRepository.isUserExistsByEmail(createUserRequestDto.email())) {
            throw new ConflictException("ERR:EMAIL_EXISTS");
        }

        UserEntity user = userMapper.toEntityFromCreateRequestDto(createUserRequestDto);
        return userMapper.toCreateResponseDto(userEntityRepository.save(user));
    }

    @Transactional
    public UpdateUserResponseDto updateEmail(
            UUID id, String email
    ) {
        UserEntity user = findEntityById(id);
        user.setEmail(email);
        UserEntity updatedUser = userEntityRepository.save(user);

        return userMapper.toUpdateUserResponseDto(updatedUser);
    }

    @Transactional UpdateUserResponseDto updatePassword(
            UUID id, String password
    ) {
        
    }
}
