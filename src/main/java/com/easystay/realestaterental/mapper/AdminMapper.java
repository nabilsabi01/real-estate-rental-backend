package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.AdminDTO;
import com.easystay.realestaterental.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminDTO toDTO(Admin admin);
    @Mapping(target = "password", ignore = true)
    Admin toEntity(AdminDTO adminDTO);
}
