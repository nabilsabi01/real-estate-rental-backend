package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByPropertyId(Long propertyId);
}
