package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.HostDTO;
import com.easystay.realestaterental.entity.Host;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PropertyMapper.class, ReviewMapper.class})
public interface HostMapper {

    @Mapping(target = "propertyIds", expression = "java(mapPropertyIds(host))")
    @Mapping(target = "receivedReviewIds", expression = "java(mapReceivedReviewIds(host))")
    HostDTO toDTO(Host host);

    @Mapping(target = "properties", ignore = true)
    @Mapping(target = "receivedReviews", ignore = true)
    @Mapping(target = "password", ignore = true)
    Host toEntity(HostDTO hostDTO);

    default List<Long> mapPropertyIds(Host host) {
        return host.getProperties().stream()
                .map(property -> property.getId())
                .collect(Collectors.toList());
    }

    default List<Long> mapReceivedReviewIds(Host host) {
        return host.getReceivedReviews().stream()
                .map(review -> review.getId())
                .collect(Collectors.toList());
    }
}