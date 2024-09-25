package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Booking;
import com.easystay.realestaterental.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByGuestId(Long guestId, Pageable pageable);
    Page<Booking> findByPropertyId(Long propertyId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.property.id = :propertyId AND " +
            "((b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate) OR " +
            "(b.checkInDate >= :checkInDate AND b.checkInDate < :checkOutDate)) AND " +
            "b.status NOT IN :excludedStatuses")
    List<Booking> findOverlappingBookings(@Param("propertyId") Long propertyId,
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate,
                                          @Param("excludedStatuses") List<BookingStatus> excludedStatuses);
}