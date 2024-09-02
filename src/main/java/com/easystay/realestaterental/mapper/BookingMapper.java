package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.BookingDTO;
import com.easystay.realestaterental.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(source = "guest.id", target = "guestId")
    BookingDTO toDTO(Booking booking);

    @Mapping(target = "property", ignore = true)
    @Mapping(target = "guest", ignore = true)
    Booking toEntity(BookingDTO bookingDTO);
}
