package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.UserDTO;
import com.easystay.realestaterental.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}
