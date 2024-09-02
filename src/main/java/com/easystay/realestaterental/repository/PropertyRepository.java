package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByHostId(Long hostId);
    List<Property> findByLocationCityIgnoreCase(String city);
    List<Property> findByLocationCountryIgnoreCase(String country);
}
