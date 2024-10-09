package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.BookingDTO;
import com.easystay.realestaterental.entity.Booking;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PropertyMapper.class, GuestMapper.class})
public interface BookingMapper {
    @Mapping(target = "propertyId", source = "property.id")
    @Mapping(target = "guestId", source = "guest.id")
    BookingDTO toDTO(Booking booking);

    @Mapping(target = "property", ignore = true)
    @Mapping(target = "guest", ignore = true)
    Booking toEntity(BookingDTO bookingDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateBookingFromDto(BookingDTO dto, @MappingTarget Booking entity);
}