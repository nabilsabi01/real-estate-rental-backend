package com.easystay.realestaterental.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GuestDTO extends UserDTO {
    private List<Long> bookingIds;
    private List<Long> writtenReviewIds;
    private List<Long> favoriteIds;
}
