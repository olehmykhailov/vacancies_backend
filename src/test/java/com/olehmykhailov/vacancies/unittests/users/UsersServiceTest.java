package com.olehmykhailov.vacancies.unittests.users;

import com.olehmykhailov.vacancies.infrastructure.exceptions.ConflictException;
import com.olehmykhailov.vacancies.infrastructure.exceptions.InvalidPasswordException;
import com.olehmykhailov.vacancies.infrastructure.exceptions.NotFoundException;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.*;
import com.olehmykhailov.vacancies.users.businesslayer.mappers.UserMapper;
import com.olehmykhailov.vacancies.users.businesslayer.services.UsersService;
import com.olehmykhailov.vacancies.users.datalayer.entities.UserEntity;
import com.olehmykhailov.vacancies.users.datalayer.repositories.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersService usersService;

    @Captor
    private ArgumentCaptor<UserEntity> userCaptor;

    private UUID testId;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testUser = new UserEntity();
        ReflectionTestUtils.setField(testUser, "id", testId);
        testUser.setEmail("old@email.com");
        testUser.setPassword("encoded_old_password");
    }


    @Test
    void createUser_shouldCreateUserSuccessfully() {
        String email = "test@email.com";
        String rawPassword = "12345";
        String encodedPassword = "encoded_12345";

        when(userEntityRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        usersService.createUser(email, rawPassword);

        verify(userEntityRepository).save(userCaptor.capture());
        UserEntity capturedUser = userCaptor.getValue();

        assertEquals(email, capturedUser.getEmail());
        assertEquals(encodedPassword, capturedUser.getPassword());
    }

    @Test
    void createUser_shouldThrowConflictException_whenEmailExists() {
        String email = "exists@email.com";
        when(userEntityRepository.existsByEmail(email)).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class, () ->
                usersService.createUser(email, "password"));

        assertEquals("ERR:EMAIL_EXISTS", exception.getMessage());
        verify(userEntityRepository, never()).save(any());
    }

    @Test
    void updateEmail_shouldUpdateEmailSuccessfully() {
        // Given
        String newEmail = "new@email.com";
        UpdateUserEmailRequestDto request = new UpdateUserEmailRequestDto(newEmail);
        UpdateUserResponseDto expectedResponse = new UpdateUserResponseDto(testId, newEmail);

        when(userEntityRepository.findUserById(testId)).thenReturn(Optional.of(testUser));
        when(userEntityRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.toUpdateUserResponseDto(any(UserEntity.class))).thenReturn(expectedResponse);

        // When
        UpdateUserResponseDto result = usersService.updateEmail(testId, request);

        // Then
        assertNotNull(result);
        assertEquals(testId, result.id());
        assertEquals(newEmail, testUser.getEmail());
        verify(userEntityRepository).save(testUser);
    }

    @Test
    void updatePassword_shouldUpdatePasswordSuccessfully() {
        String oldPass = "old_pass";
        String newPass = "new_pass";
        String newEncodedPass = "encoded_new_pass";
        UpdateUserPasswordRequestDto request = new UpdateUserPasswordRequestDto(oldPass, newPass);

        when(userEntityRepository.findUserById(testId)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(oldPass, testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPass)).thenReturn(newEncodedPass);
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(testUser);

        usersService.updatePassword(testId, request);

        assertEquals(newEncodedPass, testUser.getPassword());
        verify(userEntityRepository).save(testUser);
    }

    @Test
    void updatePassword_shouldThrowInvalidPasswordException_whenOldPasswordWrong() {
        UpdateUserPasswordRequestDto request = new UpdateUserPasswordRequestDto("wrong_pass", "new_pass");

        when(userEntityRepository.findUserById(testId)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () ->
                usersService.updatePassword(testId, request));

        verify(userEntityRepository, never()).save(any());
    }

    @Test
    void deleteUser_shouldDeleteSuccessfully() {
        when(userEntityRepository.findUserById(testId)).thenReturn(Optional.of(testUser));

        usersService.deleteUser(testId);

        verify(userEntityRepository).delete(testUser);
    }

    @Test
    void anyMethod_shouldThrowNotFoundException_whenUserDoesNotExist() {
        when(userEntityRepository.findUserById(testId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> usersService.deleteUser(testId));
        verify(userEntityRepository, never()).delete(any());
    }
}