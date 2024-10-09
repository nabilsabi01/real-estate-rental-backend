package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.ReviewDTO;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.entity.Review;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
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
    private final PropertyRepository propertyRepository;
    private final GuestRepository guestRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Property property = propertyRepository.findById(reviewDTO.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        Guest guest = guestRepository.findById(reviewDTO.getGuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        Review review = reviewMapper.toEntity(reviewDTO);
        review.setProperty(property);
        review.setGuest(guest);

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDTO(savedReview);
    }

    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        return reviewMapper.toDTO(review);
    }

    public Page<ReviewDTO> getReviewsByPropertyId(Long propertyId, Pageable pageable) {
        return reviewRepository.findByPropertyId(propertyId, pageable)
                .map(reviewMapper::toDTO);
    }

    public Page<ReviewDTO> getReviewsByGuestId(Long guestId, Pageable pageable) {
        return reviewRepository.findByGuestId(guestId, pageable)
                .map(reviewMapper::toDTO);
    }

    public Page<ReviewDTO> getReviewsByHostId(Long hostId, Pageable pageable) {
        return reviewRepository.findByPropertyHostId(hostId, pageable)
                .map(reviewMapper::toDTO);
    }

    @Transactional
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        reviewMapper.updateReviewFromDto(reviewDTO, review);
        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toDTO(updatedReview);
    }

    @Transactional
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found");
        }
        reviewRepository.deleteById(id);
    }

    public boolean isReviewOwner(Long reviewId, Long guestId) {
        return reviewRepository.findById(reviewId)
                .map(review -> review.getGuest().getId().equals(guestId))
                .orElse(false);
    }
}