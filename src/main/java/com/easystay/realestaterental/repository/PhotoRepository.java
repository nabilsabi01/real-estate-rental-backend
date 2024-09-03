package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByPropertyId(Long propertyId);

    Optional<Photo> findByPropertyIdAndPrimaryPhotoIsTrue(Long propertyId);

    @Query("SELECT p FROM Photo p WHERE p.property.id = :propertyId ORDER BY p.primaryPhoto DESC, p.id ASC")
    List<Photo> findByPropertyIdOrderByPrimaryPhotoDesc(Long propertyId);

    @Query("SELECT COUNT(p) FROM Photo p WHERE p.property.id = :propertyId")
    long countByPropertyId(Long propertyId);

    void deleteByPropertyId(Long propertyId);
}