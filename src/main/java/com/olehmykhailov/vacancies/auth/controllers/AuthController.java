package com.olehmykhailov.vacancies.auth.controllers;

import com.olehmykhailov.vacancies.auth.dtos.SignInRequestDto;
import com.olehmykhailov.vacancies.auth.dtos.SignInResponseDto;
import com.olehmykhailov.vacancies.auth.dtos.SignUpRequestDto;
import com.olehmykhailov.vacancies.auth.services.AuthService;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.GetUserResponseDto;

import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<GetUserResponseDto> signIn(
            @Valid @RequestBody SignInRequestDto signInRequestDto
            ) {
        SignInResponseDto responseDto = authService.signIn(signInRequestDto);
        GetUserResponseDto user = responseDto.user();
        String token = responseDto.token();

        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(user);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto
            ) {
        authService.signUp(signUpRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
