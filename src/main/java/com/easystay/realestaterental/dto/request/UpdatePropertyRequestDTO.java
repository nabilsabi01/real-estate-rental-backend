package com.easystay.realestaterental.dto.request;

import com.easystay.realestaterental.enums.PropertyType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdatePropertyRequestDTO {
    private String title;
    private String description;
    private BigDecimal pricePerNight;
    private Integer maxGuests;
    private Integer bedrooms;
    private Integer bathrooms;
    private PropertyType propertyType;
    private UpdateLocationRequestDTO location;
    private List<Long> amenityIds;
    private Integer minNightsStay;
    private Integer maxNightsStay;
}