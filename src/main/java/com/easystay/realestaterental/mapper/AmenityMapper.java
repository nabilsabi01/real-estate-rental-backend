package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.AmenityDTO;
import com.easystay.realestaterental.entity.Amenity;
import com.easystay.realestaterental.mapper.PropertyMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PropertyMapper.class)
public interface AmenityMapper {

    @Mapping(target = "properties.id", ignore = true)
    AmenityDTO toDTO(Amenity amenity);

    @Mapping(target = "properties", ignore = true)
    Amenity toEntity(AmenityDTO amenityDTO);
}
