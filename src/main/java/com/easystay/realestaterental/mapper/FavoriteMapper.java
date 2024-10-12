package com.easystay.realestaterental.mapper;

import com.easystay.realestaterental.dto.FavoriteDTO;
import com.easystay.realestaterental.entity.Favorite;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PropertyMapper.class})
public interface FavoriteMapper {
    @Mapping(target = "propertyId", source = "property.id")
    @Mapping(target = "guestId", source = "guest.id")
    FavoriteDTO toDTO(Favorite favorite);

    @Mapping(target = "property", ignore = true)
    @Mapping(target = "guest", ignore = true)
    Favorite toEntity(FavoriteDTO favoriteDTO);
}