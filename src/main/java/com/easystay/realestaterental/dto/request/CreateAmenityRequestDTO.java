package com.easystay.realestaterental.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAmenityRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Icon is required")
    private String icon;
}
