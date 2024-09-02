package com.easystay.realestaterental.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhotoResponseDTO {
    private Long id;
    private String url;
    private String caption;
    private boolean isPrimary;
    private LocalDateTime uploadedAt;
}
