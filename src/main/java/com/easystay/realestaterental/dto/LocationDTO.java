package com.easystay.realestaterental.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {
    private String country;
    private String city;
    private String address;
    private String postalCode;
    private Double latitude;
    private Double longitude;
}
