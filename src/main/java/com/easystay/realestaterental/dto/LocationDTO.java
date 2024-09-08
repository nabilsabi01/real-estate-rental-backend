package com.easystay.realestaterental.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class LocationDTO {
    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Address is required")
    private String address;

    private String postalCode;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;
}