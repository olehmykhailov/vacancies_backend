package com.olehmykhailov.vacancies.auth.services;

import com.olehmykhailov.vacancies.auth.dtos.SignInRequestDto;
import com.olehmykhailov.vacancies.auth.dtos.SignInResponseDto;
import com.olehmykhailov.vacancies.auth.dtos.SignUpRequestDto;
import com.olehmykhailov.vacancies.auth.dtos.SignUpResponseDto;
import com.olehmykhailov.vacancies.auth.jwt.JwtService;
import com.olehmykhailov.vacancies.infrastructure.exceptions.InvalidPasswordException;
import com.olehmykhailov.vacancies.infrastructure.exceptions.NotFoundException;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.CreateUserRequestDto;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.CreateUserResponseDto;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.GetUserResponseDto;
import com.olehmykhailov.vacancies.users.businesslayer.services.UsersService;
import com.olehmykhailov.vacancies.users.datalayer.entities.UserEntity;
import com.olehmykhailov.vacancies.users.datalayer.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsersService usersService;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) {
        UserEntity user = userEntityRepository.findByEmail(signInRequestDto.email())
                .orElseThrow(() -> new NotFoundException("ERR:EMAIL_NOT_FOUND"));
        if (!passwordEncoder.matches(signInRequestDto.password(), user.getPassword())) {
            throw new InvalidPasswordException("ERR:INVALID_PASSWORD");
        }

        return new SignInResponseDto(
                new GetUserResponseDto(
                        user.getId(),
                        user.getEmail()
                ),
                jwtService.generateToken(user.getUsername())
        );
    }

    public void signUp(SignUpRequestDto signUpRequestDto) {
        usersService.createUser(
                signUpRequestDto.email(),
                signUpRequestDto.password()
        );
    }
}
