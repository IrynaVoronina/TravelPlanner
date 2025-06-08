package com.example.backend.mapper;

import com.example.backend.dto.user.UserRequestDTO;
import com.example.backend.dto.user.UserResponseDTO;
import com.example.backend.model.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toModel(UserRequestDTO dto);

    UserResponseDTO toDto(User user);

    List<UserResponseDTO> toDtoList(List<User> users);

}