package com.easystay.realestaterental.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HostDTO extends UserDTO {
    private String bio;
    private boolean superHost;
    private List<Long> propertyIds;
    private List<Long> receivedReviewIds;
}
