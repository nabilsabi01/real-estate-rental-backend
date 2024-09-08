package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.AdminDTO;
import com.easystay.realestaterental.entity.Admin;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AdminDTO toAdminDTO(Admin admin);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Admin toAdmin(AdminDTO adminDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateAdminFromDTO(AdminDTO adminDTO, @MappingTarget Admin admin);
}