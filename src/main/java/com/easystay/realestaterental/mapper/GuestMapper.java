package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.GuestDTO;
import com.easystay.realestaterental.entity.Guest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GuestMapper {
    @Mapping(target = "bookingIds", ignore = true)
    @Mapping(target = "writtenReviewIds", ignore = true)
    @Mapping(target = "favoriteIds", ignore = true)
    GuestDTO toGuestDTO(Guest guest);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "writtenReviews", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Guest toGuest(GuestDTO guestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "writtenReviews", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateGuestFromDTO(GuestDTO guestDTO, @MappingTarget Guest guest);
}