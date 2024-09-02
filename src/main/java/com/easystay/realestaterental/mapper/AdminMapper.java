package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.AdminDTO;
import com.easystay.realestaterental.entity.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminDTO toDTO(Admin admin);
    Admin toEntity(AdminDTO adminDTO);
}
