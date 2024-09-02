package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.ReviewDTO;
import com.easystay.realestaterental.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(source = "guest.id", target = "guestId")
    @Mapping(source = "host.id", target = "hostId")
    ReviewDTO toDTO(Review review);

    @Mapping(target = "property", ignore = true)
    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "host", ignore = true)
    Review toEntity(ReviewDTO reviewDTO);
}
