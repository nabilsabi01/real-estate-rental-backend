package com.easystay.realestaterental.service;

import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Host;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.entity.Review;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.repository.GuestRepository;
import com.easystay.realestaterental.repository.HostRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import com.easystay.realestaterental.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final PropertyRepository propertyRepository;

    private final GuestRepository guestRepository;

    public ReviewService(ReviewRepository reviewRepository, PropertyRepository propertyRepository, GuestRepository guestRepository, HostRepository hostRepository) {
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
        this.guestRepository = guestRepository;
    }

    public List<Review> getReviewsByPropertyId(Long propertyId) {
        return reviewRepository.findByPropertyId(propertyId);
    }

    public List<Review> getReviewsByGuestId(Long guestId) {
        return reviewRepository.findByGuestId(guestId);
    }

    public List<Review> getReviewsByHostId(Long hostId) {
        return reviewRepository.findByHostId(hostId);
    }

    @Transactional
    public Review createReview(Long propertyId, Long guestId, int rating, String comment) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));
        Host host = property.getHost();

        Review review = new Review();
        review.setProperty(property);
        review.setGuest(guest);
        review.setHost(host);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(Long reviewId, int rating, String comment) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
    }
}