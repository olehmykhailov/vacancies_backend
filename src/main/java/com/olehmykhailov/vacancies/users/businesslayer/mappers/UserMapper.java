package com.olehmykhailov.vacancies.users.businesslayer.mappers;

import com.olehmykhailov.vacancies.users.businesslayer.dtos.CreateUserRequestDto;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.CreateUserResponseDto;
import com.olehmykhailov.vacancies.users.businesslayer.dtos.UpdateUserResponseDto;
import com.olehmykhailov.vacancies.users.datalayer.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntityFromCreateRequestDto(CreateUserRequestDto createUserRequestDto);

    CreateUserResponseDto toCreateResponseDto(UserEntity userEntity);

    UpdateUserResponseDto toUpdateUserResponseDto(UserEntity userEntity);

}
