package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.LocationDTO;
import com.easystay.realestaterental.entity.Location;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "property", ignore = true)
    Location toEntity(LocationDTO locationDTO);

    LocationDTO toDTO(Location location);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "property", ignore = true)
    void updateLocationFromDto(LocationDTO dto, @MappingTarget Location entity);
}