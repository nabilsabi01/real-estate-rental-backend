package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Property, Long> {

    @Query("SELECT DISTINCT p.location.city FROM Property p")
    List<String> findAllCities();

    @Query("SELECT DISTINCT p.location.country FROM Property p")
    List<String> findAllCountries();

    @Query("SELECT p FROM Property p WHERE p.location.city = :city")
    List<Property> findPropertiesByCity(String city);

    @Query("SELECT p FROM Property p WHERE p.location.country = :country")
    List<Property> findPropertiesByCountry(String country);

    @Query("SELECT p FROM Property p WHERE " +
            "p.location.latitude BETWEEN :minLat AND :maxLat AND " +
            "p.location.longitude BETWEEN :minLon AND :maxLon")
    List<Property> findPropertiesWithinArea(double minLat, double maxLat, double minLon, double maxLon);
}