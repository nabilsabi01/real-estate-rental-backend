package com.easystay.realestaterental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(name = "zip_code")
    private String zipCode;

    private Double latitude;

    private Double longitude;
}