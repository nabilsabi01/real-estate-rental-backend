package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPropertyId(Long propertyId);
    List<Review> findByGuestId(Long guestId);
    List<Review> findByPropertyHostId(Long hostId);
}