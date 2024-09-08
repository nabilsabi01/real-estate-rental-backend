package com.easystay.realestaterental.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class HostDTO extends UserDTO {
    @Size(max = 1000, message = "Bio must be less than 1000 characters")
    private String bio;
    private boolean superHost;
    private List<Long> propertyIds;
}
