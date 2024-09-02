package com.easystay.realestaterental.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePhotoRequestDTO {
    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotBlank(message = "URL is required")
    private String url;

    private String caption;
    private boolean isPrimary;
}
