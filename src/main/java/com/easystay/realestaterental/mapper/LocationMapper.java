package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.LocationDTO;
import com.easystay.realestaterental.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDTO toDTO(Location location);
    Location toEntity(LocationDTO locationDTO);
}
