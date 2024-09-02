package com.easystay.realestaterental.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FavoriteDTO {
    private Long id;
    private Long propertyId;
    private Long guestId;
    private LocalDateTime favoritedAt;
}
