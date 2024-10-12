package com.easystay.realestaterental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteDTO {
    private Long id;
    private Long propertyId;
    private Long guestId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime favoritedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PropertyDTO property;
}