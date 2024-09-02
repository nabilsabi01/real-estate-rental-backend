package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByGuestId(Long guestId);
    List<Booking> findByPropertyId(Long propertyId);
    List<Booking> findByPropertyIdAndCheckInDateBetweenOrCheckOutDateBetween(Long propertyId, LocalDate checkInStart, LocalDate checkInEnd, LocalDate checkOutStart, LocalDate checkOutEnd);
}
