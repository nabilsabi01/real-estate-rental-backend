package com.easystay.realestaterental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guests")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
public class Guest extends User {
    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> writtenReviews = new ArrayList<>();

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();
}
