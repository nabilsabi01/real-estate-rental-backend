package com.easystay.realestaterental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "availability_calendars")
@Getter
@Setter
public class AvailabilityCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ElementCollection
    @CollectionTable(name = "available_dates", joinColumns = @JoinColumn(name = "calendar_id"))
    private Set<LocalDate> availableDates = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "special_prices", joinColumns = @JoinColumn(name = "calendar_id"))
    @MapKeyColumn(name = "date")
    @Column(name = "price")
    private Map<LocalDate, Integer> specialPrices = new HashMap<>();
}
