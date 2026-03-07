package com.olehmykhailov.vacancies.users.businesslayer.services;

import com.olehmykhailov.vacancies.infrastructure.exceptions.ConflictException;
import com.olehmykhailov.vacancies.infrastructure.exceptions.InvalidPasswordException;
import com.olehmykhailov.vacancies.infrastructure.exceptions.NotFoundException;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.*;
import com.olehmykhailov.vacancies.users.businesslayer.mappers.UserMapper;
import com.olehmykhailov.vacancies.users.datalayer.entities.UserEntity;
import com.olehmykhailov.vacancies.users.datalayer.repositories.UserEntityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserEntityRepository userEntityRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private UserEntity findEntityById(UUID id) {
        return userEntityRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("ERR:USER_NOT_FOUND"));
    }

    @Transactional
    public UserEntity createUser(String email, String password) {
        if (userEntityRepository.isUserExistsByEmail(email)) {
            throw new ConflictException("ERR:EMAIL_EXISTS");
        }
        String passwordHash = passwordEncoder.encode(password);
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordHash);

        return userEntityRepository.save(user);
    }

    @Transactional
    public UpdateUserResponseDto updateEmail(
            UUID id, UpdateUserEmailRequestDto updateUserEmailRequestDto
    ) {
        UserEntity user = findEntityById(id);
        user.setEmail(updateUserEmailRequestDto.email());
        UserEntity updatedUser = userEntityRepository.save(user);

        return userMapper.toUpdateUserResponseDto(updatedUser);
    }

    @Transactional
    public UpdateUserResponseDto updatePassword(
            UUID id, UpdateUserPasswordRequestDto updateUserPasswordRequestDto
    ) {
        UserEntity user = findEntityById(id);

        if (!passwordEncoder.matches(user.getPassword(), updateUserPasswordRequestDto.oldPassword())) {
            throw new InvalidPasswordException("ERR:INVALID_PASSWORD");
        }

        String newPasswordHash = passwordEncoder.encode(updateUserPasswordRequestDto.newPassword());
        user.setPassword(newPasswordHash);

        UserEntity updatedUser = userEntityRepository.save(user);
        return userMapper.toUpdateUserResponseDto(updatedUser);
    }

    @Transactional
    public void deleteUser(UUID id) {
        UserEntity user = findEntityById(id);

        userEntityRepository.delete(user);
    }

}
