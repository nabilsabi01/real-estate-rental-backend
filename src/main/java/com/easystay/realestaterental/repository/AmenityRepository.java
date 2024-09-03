package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    List<Amenity> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a FROM Amenity a JOIN a.properties p WHERE p.id = :propertyId")
    List<Amenity> findByPropertyId(Long propertyId);

    @Query("SELECT a FROM Amenity a GROUP BY a ORDER BY SIZE(a.properties) DESC")
    List<Amenity> findMostPopularAmenities(Pageable pageable);
}