package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Amenity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Page<Amenity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT a FROM Amenity a JOIN a.properties p WHERE p.id = :propertyId")
    List<Amenity> findByPropertyId(Long propertyId);

    @Query("SELECT a FROM Amenity a GROUP BY a ORDER BY SIZE(a.properties) DESC")
    Page<Amenity> findMostPopularAmenities(Pageable pageable);
}