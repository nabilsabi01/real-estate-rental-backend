package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.enums.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByHostId(Long hostId);

    List<Property> findByPropertyType(PropertyType propertyType);

    List<Property> findByPricePerNightBetween(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT p FROM Property p WHERE p.location.city = :city")
    List<Property> findByCity(String city);

    @Query("SELECT p FROM Property p WHERE p.location.country = :country")
    List<Property> findByCountry(String country);

    @Query("SELECT p FROM Property p JOIN p.amenities a WHERE a.id = :amenityId")
    List<Property> findByAmenityId(Long amenityId);

    @Query("SELECT p FROM Property p WHERE p.maxGuests >= :guestCount")
    List<Property> findByGuestCapacity(int guestCount);

    @Query("SELECT p FROM Property p WHERE p.title LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Property> searchProperties(String keyword);
}