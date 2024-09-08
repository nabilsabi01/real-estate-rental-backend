package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.AmenityDTO;
import com.easystay.realestaterental.entity.Amenity;
import com.easystay.realestaterental.entity.Property;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
    @Mapping(target = "propertyIds", expression = "java(mapPropertyIds(amenity))")
    AmenityDTO toDTO(Amenity amenity);

    @Mapping(target = "properties", ignore = true)
    Amenity toEntity(AmenityDTO amenityDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "properties", ignore = true)
    void updateAmenityFromDto(AmenityDTO dto, @MappingTarget Amenity entity);

    default Set<Long> mapPropertyIds(Amenity amenity) {
        return amenity.getProperties() != null ? amenity.getProperties().stream().map(Property::getId).collect(Collectors.toSet()) : null;
    }
}