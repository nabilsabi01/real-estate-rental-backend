package com.easystay.realestaterental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PhotoDTO {
    private Long id;

    @NotBlank(message = "Photo URL is required")
    private String photoUrl;

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime uploadedAt;
}