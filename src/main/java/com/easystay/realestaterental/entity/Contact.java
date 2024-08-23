package com.easystay.realestaterental.entity;

import com.easystay.realestaterental.enums.ContactStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    @ManyToOne
    private Listing listing;

    private String message;

    @Enumerated(EnumType.STRING)
    private ContactStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
