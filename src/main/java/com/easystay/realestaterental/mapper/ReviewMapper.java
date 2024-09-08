package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.ReviewDTO;
import com.easystay.realestaterental.entity.Review;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "propertyId", source = "property.id")
    @Mapping(target = "guestId", source = "guest.id")
    ReviewDTO toDTO(Review review);

    @Mapping(target = "property", ignore = true)
    @Mapping(target = "guest", ignore = true)
    Review toEntity(ReviewDTO reviewDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "guest", ignore = true)
    void updateReviewFromDto(ReviewDTO dto, @MappingTarget Review entity);
}
