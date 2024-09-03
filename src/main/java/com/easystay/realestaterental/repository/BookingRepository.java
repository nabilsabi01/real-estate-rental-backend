package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Booking;
import com.easystay.realestaterental.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByGuestId(Long guestId);

    List<Booking> findByPropertyId(Long propertyId);

    List<Booking> findByStatus(BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.property.id = :propertyId AND ((b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate) OR (b.checkInDate >= :checkInDate AND b.checkInDate < :checkOutDate))")
    List<Booking> findConflictingBookings(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.property.host.id = :hostId AND b.status = :status AND b.checkInDate BETWEEN :startDate AND :endDate")
    long countBookingsByHostAndStatus(Long hostId, BookingStatus status, LocalDate startDate, LocalDate endDate);

    @Query("SELECT b FROM Booking b WHERE b.checkInDate >= :startDate ORDER BY b.checkInDate ASC")
    List<Booking> findUpcomingBookings(LocalDate startDate);
}