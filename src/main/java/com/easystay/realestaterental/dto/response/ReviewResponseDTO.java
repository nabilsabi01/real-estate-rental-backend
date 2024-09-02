package com.easystay.realestaterental.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponseDTO {
    private Long id;
    private PropertyResponseDTO property;
    private GuestResponseDTO guest;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
