package com.easystay.realestaterental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hosts")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
public class Host extends User {
    @Column(length = 1000)
    private String bio;

    private boolean superHost;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties = new ArrayList<>();
}
