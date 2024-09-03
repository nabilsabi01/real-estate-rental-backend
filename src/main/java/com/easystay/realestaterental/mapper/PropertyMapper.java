package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.entity.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {HostMapper.class, LocationMapper.class, AmenityMapper.class, PhotoMapper.class, ReviewMapper.class, BookingMapper.class})
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

    default List<Long> mapAmenityIds(Property property) {
        return property.getAmenities().stream()
                .map(amenity -> amenity.getId())
                .collect(Collectors.toList());
    }

    default List<Long> mapPhotoIds(Property property) {
        return property.getPhotos().stream()
                .map(photo -> photo.getId())
                .collect(Collectors.toList());
    }

    default List<Long> mapReviewIds(Property property) {
        return property.getReviews().stream()
                .map(review -> review.getId())
                .collect(Collectors.toList());
    }

    default List<Long> mapBookingIds(Property property) {
        return property.getBookings().stream()
                .map(booking -> booking.getId())
                .collect(Collectors.toList());
    }

    default List<Long> mapFavoriteIds(Property property) {
        return property.getFavorites().stream()
                .map(favorite -> favorite.getId())
                .collect(Collectors.toList());
    }
}