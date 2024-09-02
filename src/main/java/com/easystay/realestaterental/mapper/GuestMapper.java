package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.GuestDTO;
import com.easystay.realestaterental.entity.Guest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    @Mapping(source = "bookings.id", target = "bookingIds")
    @Mapping(source = "writtenReviews.id", target = "writtenReviewIds")
    @Mapping(source = "favorites.id", target = "favoriteIds")
    GuestDTO toDTO(Guest guest);

    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "writtenReviews", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    Guest toEntity(GuestDTO guestDTO);
}
