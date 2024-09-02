package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.AmenityDTO;
import com.easystay.realestaterental.entity.Amenity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
    AmenityDTO toDTO(Amenity amenity);
    Amenity toEntity(AmenityDTO amenityDTO);
}
