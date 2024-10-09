package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.entity.Property;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, PhotoMapper.class})
public interface PropertyMapper {

    @Mapping(target = "host", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    Property toEntity(PropertyDTO dto);

    @Mapping(target = "hostId", source = "host.id")
    @Mapping(target = "amenityIds", expression = "java(property.getAmenities().stream().map(amenity -> amenity.getId()).collect(java.util.stream.Collectors.toSet()))")
    @Mapping(target = "isFavorited", ignore = true)
    PropertyDTO toDTO(Property property);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "host", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updatePropertyFromDto(PropertyDTO dto, @MappingTarget Property entity);
}