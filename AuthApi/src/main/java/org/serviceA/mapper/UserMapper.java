package org.serviceA.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.serviceA.dto.user.UserRegistrationRequestDto;
import org.serviceA.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "passwordHash", source = "password")
    User toModel(UserRegistrationRequestDto requestDto);
}
