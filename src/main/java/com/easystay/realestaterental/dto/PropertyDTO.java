package com.easystay.realestaterental.dto;

import com.easystay.realestaterental.enums.PropertyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PropertyDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Location is required")
    private LocationDTO location;

    @NotNull(message = "Price per night is required")
    @Positive(message = "Price per night must be positive")
    private BigDecimal pricePerNight;

    @Positive(message = "Max guests must be positive")
    private Integer maxGuests;

    @Positive(message = "Number of bedrooms must be positive")
    private Integer bedrooms;

    @Positive(message = "Number of beds must be positive")
    private Integer beds;

    @Positive(message = "Number of bathrooms must be positive")
    private Integer bathrooms;

    @NotNull(message = "Property type is required")
    private PropertyType propertyType;

    @NotNull(message = "Host ID is required")
    private Long hostId;

    private List<Long> amenityIds;
    private List<Long> photoIds;
    private List<Long> reviewIds;
    private List<Long> bookingIds;
    private List<Long> favoriteIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
}