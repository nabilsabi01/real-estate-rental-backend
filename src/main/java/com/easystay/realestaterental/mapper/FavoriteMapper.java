package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.FavoriteDTO;
import com.easystay.realestaterental.entity.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(source = "guest.id", target = "guestId")
    FavoriteDTO toDTO(Favorite favorite);

    @Mapping(target = "property", ignore = true)
    @Mapping(target = "guest", ignore = true)
    Favorite toEntity(FavoriteDTO favoriteDTO);
}
