package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.HostDTO;
import com.easystay.realestaterental.entity.Host;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HostMapper {

    @Mapping(source = "properties.id", target = "propertyIds")
    @Mapping(source = "receivedReviews.id", target = "receivedReviewIds")
    HostDTO toDTO(Host host);

    @Mapping(target = "properties", ignore = true)
    @Mapping(target = "receivedReviews", ignore = true)
    Host toEntity(HostDTO hostDTO);
}
