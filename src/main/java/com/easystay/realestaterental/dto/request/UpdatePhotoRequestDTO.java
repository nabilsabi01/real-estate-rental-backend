package com.easystay.realestaterental.dto.request;

import lombok.Data;

@Data
public class UpdatePhotoRequestDTO {
    private String caption;
    private boolean isPrimary;
}