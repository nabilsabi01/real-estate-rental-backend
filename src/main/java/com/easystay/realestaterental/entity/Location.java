package com.easystay.realestaterental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locations")
@Getter @Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String city;
    private String address;
    private String postalCode;
    private Double latitude;
    private Double longitude;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private Property property;
}
