package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.PhotoDTO;
import com.easystay.realestaterental.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    @Mapping(source = "property.id", target = "propertyId")
    PhotoDTO toDTO(Photo photo);

    @Mapping(target = "property", ignore = true)
    Photo toEntity(PhotoDTO photoDTO);
}
