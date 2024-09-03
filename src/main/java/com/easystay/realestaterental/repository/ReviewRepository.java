package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByPropertyId(Long propertyId, Pageable pageable);

    List<Review> findByGuestId(Long guestId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.property.id = :propertyId")
    Double getAverageRatingForProperty(Long propertyId);

    @Query("SELECT r FROM Review r WHERE r.property.host.id = :hostId")
    List<Review> findReviewsForHost(Long hostId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.property.id = :propertyId AND r.rating >= :minRating")
    long countPositiveReviews(Long propertyId, int minRating);
}