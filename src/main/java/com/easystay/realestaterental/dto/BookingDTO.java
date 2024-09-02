package com.easystay.realestaterental.dto;

import com.easystay.realestaterental.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDTO {
    private Long id;
    private Long propertyId;
    private Long guestId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus status;
    private BigDecimal totalPrice;
    private Integer numberOfGuests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
