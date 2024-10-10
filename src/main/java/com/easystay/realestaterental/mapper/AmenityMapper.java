package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.AmenityDTO;
import com.easystay.realestaterental.entity.Amenity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
    @Mapping(target = "propertyIds", expression = "java(amenity.getProperties().stream().map(property -> property.getId()).collect(java.util.stream.Collectors.toSet()))")
    AmenityDTO toDTO(Amenity amenity);

    @Mapping(target = "properties", ignore = true)
    Amenity toEntity(AmenityDTO amenityDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "properties", ignore = true)
    void updateAmenityFromDto(AmenityDTO dto, @MappingTarget Amenity entity);
}