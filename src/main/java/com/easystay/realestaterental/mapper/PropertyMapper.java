package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.entity.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    @Mapping(source = "amenities.id", target = "amenityIds")
    @Mapping(source = "photos.id", target = "photoIds")
    @Mapping(source = "reviews.id", target = "reviewIds")
    @Mapping(source = "bookings.id", target = "bookingIds")
    @Mapping(source = "host.id", target = "hostId")
    PropertyDTO toDTO(Property property);

    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "host", ignore = true)
    Property toEntity(PropertyDTO propertyDTO);
}
