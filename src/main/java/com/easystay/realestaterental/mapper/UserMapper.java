package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.UserDTO;
import com.easystay.realestaterental.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);

    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO userDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserFromDto(UserDTO dto, @MappingTarget User entity);
}


