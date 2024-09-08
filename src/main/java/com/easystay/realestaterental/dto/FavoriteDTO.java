package com.easystay.realestaterental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class FavoriteDTO {
    private Long id;

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Guest ID is required")
    private Long guestId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime favoritedAt;
}