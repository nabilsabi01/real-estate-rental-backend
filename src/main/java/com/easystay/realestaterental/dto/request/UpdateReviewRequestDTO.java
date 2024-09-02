package com.easystay.realestaterental.dto.request;

import lombok.Data;

@Data
public class UpdateReviewRequestDTO {
    private Integer rating;
    private String comment;
}
