package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.PhotoDTO;
import com.easystay.realestaterental.entity.Photo;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PhotoMapper {
    @Mapping(target = "propertyId", source = "property.id")
    PhotoDTO toDTO(Photo photo);

    @Mapping(target = "property", ignore = true)
    Photo toEntity(PhotoDTO photoDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "property", ignore = true)
    void updatePhotoFromDto(PhotoDTO dto, @MappingTarget Photo entity);
}
