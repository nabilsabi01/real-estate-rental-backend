package com.easystay.realestaterental.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GuestDTO extends UserDTO {
    private List<Long> bookingIds;
    private List<Long> writtenReviewIds;
    private List<Long> favoriteIds;
}
