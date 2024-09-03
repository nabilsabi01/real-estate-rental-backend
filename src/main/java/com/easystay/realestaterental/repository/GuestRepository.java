package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByLastName(String lastName);

    @Query("SELECT g FROM Guest g WHERE g.firstName LIKE %:keyword% OR g.lastName LIKE %:keyword%")
    List<Guest> searchGuests(String keyword);

    @Query("SELECT g FROM Guest g JOIN g.bookings b WHERE b.property.id = :propertyId")
    List<Guest> findGuestsByPropertyId(Long propertyId);

    @Query("SELECT COUNT(DISTINCT g) FROM Guest g JOIN g.bookings b WHERE b.property.host.id = :hostId")
    long countUniqueGuestsByHostId(Long hostId);
}