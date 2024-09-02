package com.easystay.realestaterental.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class Location {
    private String country;
    private String city;
    private String address;
    private String postalCode;
    private Double latitude;
    private Double longitude;
}