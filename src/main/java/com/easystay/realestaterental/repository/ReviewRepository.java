package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByPropertyId(Long propertyId, Pageable pageable);
    Page<Review> findByGuestId(Long guestId, Pageable pageable);
    boolean existsByGuestIdAndPropertyId(Long guestId, Long propertyId);
}