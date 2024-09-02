package com.easystay.realestaterental.dto.request;

import lombok.Data;

@Data
public class UpdateLocationRequestDTO {
    private String country;
    private String city;
    private String address;
    private String postalCode;
    private Double latitude;
    private Double longitude;
}
