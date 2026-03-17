package com.olehmykhailov.vacancies.users.businesslayer.controllers;

import com.olehmykhailov.vacancies.users.businesslayer.dtos.UpdateUserEmailRequestDto;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.UpdateUserPasswordRequestDto;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.UpdateUserResponseDto;
import com.olehmykhailov.vacancies.users.businesslayer.services.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @PatchMapping("/{id}/password")
    public ResponseEntity<UpdateUserResponseDto> updateUserPassword(
            @PathVariable(name = "id") UUID id,
            @Valid @RequestBody UpdateUserPasswordRequestDto userPasswordRequestDto
    ) {
        UpdateUserResponseDto response = usersService.updatePassword(id, userPasswordRequestDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<UpdateUserResponseDto> updateUserEmail(
            @PathVariable(name = "id") UUID id,
            @Valid @RequestBody UpdateUserEmailRequestDto updateUserEmailRequestDto
    ) {
        UpdateUserResponseDto response = usersService.updateEmail(id, updateUserEmailRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable(name = "id") UUID id
    ) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
