package com.easystay.realestaterental.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PhotoDTO {
    private Long id;
    private String url;
    private String caption;
    private boolean primaryPhoto;
    private Long propertyId;
    private LocalDateTime uploadedAt;
}
