package com.easystay.realestaterental.dto.response;

import lombok.Data;

@Data
public class LocationResponseDTO {
    private Long id;
    private String country;
    private String city;
    private String address;
    private String postalCode;
    private Double latitude;
    private Double longitude;
}
