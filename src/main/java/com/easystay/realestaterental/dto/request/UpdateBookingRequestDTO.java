package com.easystay.realestaterental.dto.request;

import com.easystay.realestaterental.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateBookingRequestDTO {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfGuests;
    private BookingStatus status;
}
