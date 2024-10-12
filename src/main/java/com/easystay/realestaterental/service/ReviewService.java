package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.ReviewDTO;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.entity.Review;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.exception.UnauthorizedException;
import com.easystay.realestaterental.mapper.ReviewMapper;
import com.easystay.realestaterental.repository.GuestRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import com.easystay.realestaterental.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final GuestRepository guestRepository;
    private final PropertyRepository propertyRepository;
    private final ReviewMapper reviewMapper;

    /**
     * Adds a new review for a property.
     *
     * @param guestId    the ID of the guest creating the review
     * @param propertyId the ID of the property being reviewed
     * @param reviewDTO  the review data
     * @return the created review DTO
     */
    @Transactional
    public ReviewDTO addReview(Long guestId, Long propertyId, ReviewDTO reviewDTO) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        if (reviewRepository.existsByGuestIdAndPropertyId(guestId, propertyId)) {
            throw new IllegalStateException("Guest has already reviewed this property");
        }

        Review review = reviewMapper.toEntity(reviewDTO);
        review.setGuest(guest);
        review.setProperty(property);

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDTO(savedReview);
    }

    /**
     * Retrieves reviews for a specific property.
     *
     * @param propertyId the ID of the property
     * @param pageable   the pagination information
     * @return a page of review DTOs
     */
    public Page<ReviewDTO> getReviewsForProperty(Long propertyId, Pageable pageable) {
        return reviewRepository.findByPropertyId(propertyId, pageable)
                .map(reviewMapper::toDTO);
    }

    /**
     * Updates an existing review.
     *
     * @param guestId   the ID of the guest updating the review
     * @param reviewId  the ID of the review to update
     * @param reviewDTO the updated review data
     * @return the updated review DTO
     */
    @Transactional
    public ReviewDTO updateReview(Long guestId, Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getGuest().getId().equals(guestId)) {
            throw new UnauthorizedException("Not authorized to update this review");
        }

        reviewMapper.updateReviewFromDto(reviewDTO, review);
        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toDTO(updatedReview);
    }

    /**
     * Deletes a review.
     *
     * @param guestId  the ID of the guest deleting the review
     * @param reviewId the ID of the review to delete
     */
    @Transactional
    public void deleteReview(Long guestId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getGuest().getId().equals(guestId)) {
            throw new UnauthorizedException("Not authorized to delete this review");
        }

        reviewRepository.delete(review);
    }
}