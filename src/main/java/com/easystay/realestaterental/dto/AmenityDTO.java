package com.easystay.realestaterental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class AmenityDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Amenity name is required")
    private String name;

    @NotBlank(message = "Amenity icon is required")
    private String icon;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> propertyIds;
}