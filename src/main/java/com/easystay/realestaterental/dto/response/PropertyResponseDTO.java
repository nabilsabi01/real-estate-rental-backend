package com.easystay.realestaterental.dto.response;

import com.easystay.realestaterental.enums.PropertyType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PropertyResponseDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal pricePerNight;
    private Integer maxGuests;
    private Integer bedrooms;
    private Integer bathrooms;
    private PropertyType propertyType;
    private HostResponseDTO host;
    private LocationResponseDTO location;
    private List<AmenityResponseDTO> amenities;
    private List<PhotoResponseDTO> photos;
    private Integer minNightsStay;
    private Integer maxNightsStay;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
