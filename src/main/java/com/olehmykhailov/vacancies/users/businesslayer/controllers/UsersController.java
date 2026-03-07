package com.olehmykhailov.vacancies.users.businesslayer.controllers;

import com.olehmykhailov.vacancies.users.businesslayer.dtos.UpdateUserEmailRequestDto;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.UpdateUserPasswordRequestDto;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.UpdateUserResponseDto;
import com.olehmykhailov.vacancies.users.businesslayer.services.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @PatchMapping("/{id}/password")
    public UpdateUserResponseDto updateUserPassword(
            @PathVariable(name = "id") UUID id,
            @Valid @RequestBody UpdateUserPasswordRequestDto userPasswordRequestDto
            ) {
        return usersService.updatePassword(id, userPasswordRequestDto);
    }

    @PatchMapping("/{id}/email")
    public UpdateUserResponseDto updateUserEmail(
            @PathVariable(name = "id") UUID id,
            @Valid @RequestBody UpdateUserEmailRequestDto updateUserEmailRequestDto
            ) {
        return usersService.updateEmail(id, updateUserEmailRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable(name = "id") UUID id
    ) {
        usersService.deleteUser(id);
    }
}
