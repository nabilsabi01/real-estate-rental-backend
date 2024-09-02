package com.easystay.realestaterental.dto.request;

import com.easystay.realestaterental.enums.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreatePropertyRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price per night is required")
    @Positive(message = "Price per night must be positive")
    private BigDecimal pricePerNight;

    @NotNull(message = "Max guests is required")
    @Positive(message = "Max guests must be positive")
    private Integer maxGuests;

    @NotNull(message = "Number of bedrooms is required")
    @PositiveOrZero(message = "Number of bedrooms must be non-negative")
    private Integer bedrooms;

    @NotNull(message = "Number of bathrooms is required")
    @PositiveOrZero(message = "Number of bathrooms must be non-negative")
    private Integer bathrooms;

    @NotNull(message = "Property type is required")
    private PropertyType propertyType;

    @NotNull(message = "Host ID is required")
    private Long hostId;

    @NotNull(message = "Location is required")
    private CreateLocationRequestDTO location;

    private List<Long> amenityIds;

    @NotNull(message = "Minimum nights stay is required")
    @Positive(message = "Minimum nights stay must be positive")
    private Integer minNightsStay;

    private Integer maxNightsStay;
}
