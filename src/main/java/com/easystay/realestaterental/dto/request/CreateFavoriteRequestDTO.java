package com.easystay.realestaterental.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFavoriteRequestDTO {
    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Guest ID is required")
    private Long guestId;
}
