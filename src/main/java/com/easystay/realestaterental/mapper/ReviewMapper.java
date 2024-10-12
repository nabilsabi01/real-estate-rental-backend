package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.ReviewDTO;
import com.easystay.realestaterental.entity.Review;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "guestId", source = "guest.id")
    @Mapping(target = "propertyId", source = "property.id")
    ReviewDTO toDTO(Review review);

    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Review toEntity(ReviewDTO reviewDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateReviewFromDto(ReviewDTO dto, @MappingTarget Review entity);
}