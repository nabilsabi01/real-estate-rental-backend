package com.easystay.realestaterental.dto.response;

import com.easystay.realestaterental.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingResponseDTO {
    private Long id;
    private PropertyResponseDTO property;
    private GuestResponseDTO guest;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus status;
    private BigDecimal totalPrice;
    private Integer numberOfGuests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
