package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.GuestDTO;
import com.easystay.realestaterental.entity.Guest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {BookingMapper.class, ReviewMapper.class, FavoriteMapper.class})
public interface GuestMapper {

    @Mapping(target = "bookingIds", expression = "java(mapBookingIds(guest))")
    @Mapping(target = "writtenReviewIds", expression = "java(mapWrittenReviewIds(guest))")
    @Mapping(target = "favoriteIds", expression = "java(mapFavoriteIds(guest))")
    GuestDTO toDTO(Guest guest);

    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "writtenReviews", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "password", ignore = true)
    Guest toEntity(GuestDTO guestDTO);

    default List<Long> mapBookingIds(Guest guest) {
        return guest.getBookings().stream()
                .map(booking -> booking.getId())
                .collect(Collectors.toList());
    }

    default List<Long> mapWrittenReviewIds(Guest guest) {
        return guest.getWrittenReviews().stream()
                .map(review -> review.getId())
                .collect(Collectors.toList());
    }

    default List<Long> mapFavoriteIds(Guest guest) {
        return guest.getFavorites().stream()
                .map(favorite -> favorite.getId())
                .collect(Collectors.toList());
    }
}