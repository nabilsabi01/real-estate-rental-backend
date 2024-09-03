package com.easystay.realestaterental.dto;

import com.easystay.realestaterental.enums.PropertyType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PropertyDTO {
    private Long id;
    private String title;
    private String description;
    private LocationDTO location;
    private BigDecimal pricePerNight;
    private Integer maxGuests;
    private Integer bedrooms;
    private Integer beds;
    private Integer bathrooms;
    private PropertyType propertyType;
    private Long hostId;
    private List<Long> amenityIds;
    private List<Long> photoIds;
    private List<Long> reviewIds;
    private List<Long> bookingIds;
    private List<Long> favoriteIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}