package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.enums.PropertyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    Page<Property> findByHostId(Long hostId, Pageable pageable);

    Page<Property> findByPropertyType(PropertyType propertyType, Pageable pageable);

    Page<Property> findByPricePerNightBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Property p WHERE p.location.city = :city")
    Page<Property> findByCity(String city, Pageable pageable);

    @Query("SELECT p FROM Property p WHERE p.location.country = :country")
    Page<Property> findByCountry(String country, Pageable pageable);

    @Query("SELECT p FROM Property p JOIN p.amenities a WHERE a.id = :amenityId")
    Page<Property> findByAmenityId(Long amenityId, Pageable pageable);

    @Query("SELECT p FROM Property p WHERE p.maxGuests >= :guestCount")
    Page<Property> findByGuestCapacity(int guestCount, Pageable pageable);

    @Query("SELECT p FROM Property p WHERE p.title LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Property> searchProperties(String keyword, Pageable pageable);

    List<Property> findTop10ByOrderByCreatedAtDesc();
}