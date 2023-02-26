package com.example.instazoo.model.mapper;

import com.example.instazoo.model.dto.UserDto;
import com.example.instazoo.model.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserDto userToUserDTO(User user);
}
