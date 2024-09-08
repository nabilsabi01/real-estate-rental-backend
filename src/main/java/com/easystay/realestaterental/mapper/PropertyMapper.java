package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.entity.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {HostMapper.class, LocationMapper.class, AmenityMapper.class, PhotoMapper.class, ReviewMapper.class, BookingMapper.class, FavoriteMapper.class})
public interface PropertyMapper {
    @Mapping(source = "host.id", target = "hostId")
    @Mapping(target = "amenityIds", expression = "java(mapAmenityIds(property))")
    @Mapping(target = "photoIds", expression = "java(mapPhotoIds(property))")
    @Mapping(target = "reviewIds", expression = "java(mapReviewIds(property))")
    @Mapping(target = "bookingIds", expression = "java(mapBookingIds(property))")
    @Mapping(target = "favoriteIds", expression = "java(mapFavoriteIds(property))")
    PropertyDTO toDTO(Property property);

    @Mapping(target = "host", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    Property toEntity(PropertyDTO propertyDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "host", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    void updatePropertyFromDto(PropertyDTO dto, @MappingTarget Property entity);

    default List<Long> mapAmenityIds(Property property) {
        return property.getAmenities() != null
                ? property.getAmenities().stream().map(Amenity::getId).collect(Collectors.toList())
                : null;
    }

    default List<Long> mapPhotoIds(Property property) {
        return property.getPhotos() != null
                ? property.getPhotos().stream().map(Photo::getId).collect(Collectors.toList())
                : null;
    }

    default List<Long> mapReviewIds(Property property) {
        return property.getReviews() != null
                ? property.getReviews().stream().map(Review::getId).collect(Collectors.toList())
                : null;
    }

    default List<Long> mapBookingIds(Property property) {
        return property.getBookings() != null
                ? property.getBookings().stream().map(Booking::getId).collect(Collectors.toList())
                : null;
    }

    default List<Long> mapFavoriteIds(Property property) {
        return property.getFavorites() != null
                ? property.getFavorites().stream().map(Favorite::getId).collect(Collectors.toList())
                : null;
    }

    @AfterMapping
    default void mapAmenities(PropertyDTO dto, @MappingTarget Property property) {
        if (dto.getAmenityIds() != null) {
            Set<Amenity> amenities = dto.getAmenityIds().stream()
                    .map(id -> {
                        Amenity amenity = new Amenity();
                        amenity.setId(id);
                        return amenity;
                    })
                    .collect(Collectors.toSet());
            property.setAmenities(amenities);
        }
    }
}