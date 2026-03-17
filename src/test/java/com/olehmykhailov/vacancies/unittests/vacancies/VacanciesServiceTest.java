package com.olehmykhailov.vacancies.unittests.vacancies;

import com.olehmykhailov.vacancies.infrastructure.exceptions.NotFoundException;
import com.olehmykhailov.vacancies.users.datalayer.repositories.UserEntityRepository;
import com.olehmykhailov.vacancies.vacancies.businesslayer.dtos.*;
import com.olehmykhailov.vacancies.vacancies.businesslayer.mappers.VacanciesMapper;
import com.olehmykhailov.vacancies.vacancies.businesslayer.services.VacanciesService;
import com.olehmykhailov.vacancies.vacancies.datalayer.entities.VacancyEntity;
import com.olehmykhailov.vacancies.vacancies.datalayer.enums.VacancyStatusEnum;
import com.olehmykhailov.vacancies.vacancies.datalayer.repositories.VacancyEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VacanciesServiceTest {

    @Mock
    private VacancyEntityRepository vacancyEntityRepository;

    @Mock
    private VacanciesMapper vacanciesMapper;

    @Mock
    private UserEntityRepository userEntityRepository;

    @InjectMocks
    private VacanciesService vacanciesService;

    @Test
    void getUserVacancies_UserExists_ReturnsPageOfVacancies() {
        // Arrange
        UUID userId = UUID.randomUUID();
        int page = 0;
        int size = 10;
        
        when(userEntityRepository.existsById(userId)).thenReturn(true);

        VacancyEntity vacancyEntity = new VacancyEntity();
        vacancyEntity.setId(UUID.randomUUID());
        vacancyEntity.setTitle("Java Developer");
        vacancyEntity.setStatus(VacancyStatusEnum.SENT);
        
        Page<VacancyEntity> mockedPage = new PageImpl<>(List.of(vacancyEntity));
        
        when(vacancyEntityRepository.findVacanciesByUserId(eq(userId), any(Pageable.class)))
                .thenReturn(mockedPage);

        GetVacancyResponseDto responseDto = new GetVacancyResponseDto(
                vacancyEntity.getId(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Java Developer",
                "Description",
                "Company",
                VacancyStatusEnum.SENT,
                "http://example.com",
                Collections.emptySet()
        );

        when(vacanciesMapper.toGetResponseDtoFromEntity(vacancyEntity)).thenReturn(responseDto);

        // Act
        Page<GetVacancyResponseDto> result = vacanciesService.getUserVacancies(userId, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Java Developer", result.getContent().get(0).title());
        
        verify(userEntityRepository).existsById(userId);
        verify(vacancyEntityRepository).findVacanciesByUserId(eq(userId), any(Pageable.class));
        verify(vacanciesMapper).toGetResponseDtoFromEntity(vacancyEntity);
    }

    @Test
    void getUserVacancies_UserNotFound_ThrowsNotFoundException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        int page = 0;
        int size = 10;
        
        when(userEntityRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> 
            vacanciesService.getUserVacancies(userId, page, size)
        );
        
        verify(vacancyEntityRepository, never()).findVacanciesByUserId(any(), any());
    }

    @Test
    void createVacancy_ValidInput_ReturnsResponseDto() {
        // Arrange
        CreateVacancyRequestDto requestDto = new CreateVacancyRequestDto(
                "Java Developer",
                "Description",
                "Company",
                "http://example.com",
                Set.of("Java", "Spring")
        );

        VacancyEntity mappedEntity = new VacancyEntity();
        mappedEntity.setTitle("Java Developer");
        mappedEntity.setStatus(VacancyStatusEnum.SENT);
        
        VacancyEntity savedEntity = new VacancyEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setTitle("Java Developer");
        savedEntity.setStatus(VacancyStatusEnum.SENT);

        when(vacanciesMapper.toEntityFromCreateRequestDto(requestDto)).thenReturn(mappedEntity);
        when(vacancyEntityRepository.save(mappedEntity)).thenReturn(savedEntity);

        CreateVacancyResponseDto responseDto = new CreateVacancyResponseDto(
                savedEntity.getId(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Java Developer",
                "Description",
                "Company",
                VacancyStatusEnum.SENT,
                "http://example.com",
                Set.of("Java", "Spring")
        );
        when(vacanciesMapper.toCreateResponseDtoFromEntity(savedEntity)).thenReturn(responseDto);

        // Act
        CreateVacancyResponseDto result = vacanciesService.createVacancy(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(savedEntity.getId(), result.id());
        assertEquals("Java Developer", result.title());
        
        verify(vacanciesMapper).toEntityFromCreateRequestDto(requestDto);
        verify(vacancyEntityRepository).save(mappedEntity);
        verify(vacanciesMapper).toCreateResponseDtoFromEntity(savedEntity);
    }

    @Test
    void updateStatus_VacancyExists_ReturnsUpdatedDto() {
        // Arrange
        UUID vacancyId = UUID.randomUUID();
        VacancyStatusEnum newStatus = VacancyStatusEnum.TECH_INTERVIEW;
        
        VacancyEntity existingVacancy = new VacancyEntity();
        existingVacancy.setId(vacancyId);
        existingVacancy.setStatus(VacancyStatusEnum.SENT);
        
        when(vacancyEntityRepository.findVacancyById(vacancyId)).thenReturn(Optional.of(existingVacancy));
        
        when(vacancyEntityRepository.save(existingVacancy)).thenReturn(existingVacancy);
        
        PatchVacancyResponseDto responseDto = new PatchVacancyResponseDto(
                vacancyId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Title",
                "Desc",
                "Comp",
                newStatus,
                "url",
                Collections.emptySet()
        );
        
        when(vacanciesMapper.toPatchResponseDtoFromEntity(existingVacancy)).thenReturn(responseDto);

        // Act
        PatchVacancyResponseDto result = vacanciesService.updateStatus(vacancyId, newStatus);

        // Assert
        assertNotNull(result);
        assertEquals(newStatus, result.status());
        assertEquals(newStatus, existingVacancy.getStatus());
        
        verify(vacancyEntityRepository).findVacancyById(vacancyId);
        verify(vacancyEntityRepository).save(existingVacancy);
    }

    @Test
    void updateStatus_VacancyNotFound_ThrowsNotFoundException() {
        // Arrange
        UUID vacancyId = UUID.randomUUID();
        when(vacancyEntityRepository.findVacancyById(vacancyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> 
            vacanciesService.updateStatus(vacancyId, VacancyStatusEnum.TECH_INTERVIEW)
        );
        
        verify(vacancyEntityRepository, never()).save(any());
    }

    @Test
    void updateVacancy_VacancyExists_ReturnsUpdatedDto() {
        // Arrange
        UUID vacancyId = UUID.randomUUID();
        PatchVacancyRequestDto requestDto = new PatchVacancyRequestDto(
                "New Title",
                "New Desc",
                "New Comp",
                "New URL",
                new HashSet<>(Set.of("Python"))
        );

        VacancyEntity existingVacancy = new VacancyEntity();
        existingVacancy.setId(vacancyId);
        existingVacancy.setTitle("Old Title");

        when(vacancyEntityRepository.findVacancyById(vacancyId)).thenReturn(Optional.of(existingVacancy));
        
        doAnswer(invocation -> {
            VacancyEntity entity = invocation.getArgument(1);
            entity.setTitle("New Title");
            return null;
        }).when(vacanciesMapper).updateEntityFromDto(requestDto, existingVacancy);

        when(vacancyEntityRepository.save(existingVacancy)).thenReturn(existingVacancy);

        PatchVacancyResponseDto responseDto = new PatchVacancyResponseDto(
                vacancyId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "New Title",
                "New Desc",
                "New Comp",
                VacancyStatusEnum.SENT,
                "New URL",
                Set.of("Python")
        );
        when(vacanciesMapper.toPatchResponseDtoFromEntity(existingVacancy)).thenReturn(responseDto);

        // Act
        PatchVacancyResponseDto result = vacanciesService.updateVacancy(vacancyId, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals("New Title", result.title());
        
        verify(vacancyEntityRepository).findVacancyById(vacancyId);
        verify(vacanciesMapper).updateEntityFromDto(requestDto, existingVacancy);
        verify(vacancyEntityRepository).save(existingVacancy);
    }

    @Test
    void updateVacancy_VacancyNotFound_ThrowsNotFoundException() {
        // Arrange
        UUID vacancyId = UUID.randomUUID();
        PatchVacancyRequestDto requestDto = new PatchVacancyRequestDto(
                "Title", null, null, null, null
        );
        
        when(vacancyEntityRepository.findVacancyById(vacancyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> 
            vacanciesService.updateVacancy(vacancyId, requestDto)
        );
        
        verify(vacanciesMapper, never()).updateEntityFromDto(any(), any());
        verify(vacancyEntityRepository, never()).save(any());
    }

    @Test
    void deleteVacancy_VacancyExists_DeletesVacancy() {
        // Arrange
        UUID vacancyId = UUID.randomUUID();
        VacancyEntity existingVacancy = new VacancyEntity();
        existingVacancy.setId(vacancyId);

        when(vacancyEntityRepository.findVacancyById(vacancyId)).thenReturn(Optional.of(existingVacancy));

        // Act
        vacanciesService.deleteVacancy(vacancyId);

        // Assert
        verify(vacancyEntityRepository).findVacancyById(vacancyId);
        verify(vacancyEntityRepository).delete(existingVacancy);
    }

    @Test
    void deleteVacancy_VacancyNotFound_ThrowsNotFoundException() {
        // Arrange
        UUID vacancyId = UUID.randomUUID();
        when(vacancyEntityRepository.findVacancyById(vacancyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> 
            vacanciesService.deleteVacancy(vacancyId)
        );
        
        verify(vacancyEntityRepository, never()).delete(any());
    }
}
