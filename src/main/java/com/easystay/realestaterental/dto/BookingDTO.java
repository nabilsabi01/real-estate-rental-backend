package com.easystay.realestaterental.dto;

import com.easystay.realestaterental.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long id;

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Guest ID is required")
    private Long guestId;

    @NotNull(message = "Check-in date is required")
    @Future(message = "Check-in date must be in the future")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;

    private BookingStatus status;

    @NotNull(message = "Total price is required")
    @Positive(message = "Total price must be positive")
    private BigDecimal totalPrice;

    @Positive(message = "Number of guests must be positive")
    private Integer numberOfGuests;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
}