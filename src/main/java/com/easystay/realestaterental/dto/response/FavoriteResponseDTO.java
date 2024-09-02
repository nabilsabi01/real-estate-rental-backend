package com.easystay.realestaterental.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteResponseDTO {
    private Long id;
    private PropertyResponseDTO property;
    private GuestResponseDTO guest;
    private LocalDateTime favoritedAt;
}
