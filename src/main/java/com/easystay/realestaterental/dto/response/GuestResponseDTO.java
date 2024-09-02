package com.easystay.realestaterental.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GuestResponseDTO extends UserResponseDTO {
    private List<Long> bookingIds;
    private List<Long> reviewIds;
    private List<Long> favoriteIds;
}
