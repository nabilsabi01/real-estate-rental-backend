package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.ReviewDTO;
import com.easystay.realestaterental.entity.Review;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.ReviewMapper;
import com.easystay.realestaterental.repository.ReviewRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import com.easystay.realestaterental.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final GuestRepository guestRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Review review = reviewMapper.toEntity(reviewDTO);
        review.setProperty(propertyRepository.findById(reviewDTO.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found")));
        review.setGuest(guestRepository.findById(reviewDTO.getGuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found")));

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDTO(savedReview);
    }

    public List<ReviewDTO> getReviewsByPropertyId(Long propertyId) {
        return reviewRepository.findByPropertyId(propertyId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByGuestId(Long guestId) {
        return reviewRepository.findByGuestId(guestId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByHostId(Long hostId) {
        return reviewRepository.findByPropertyHostId(hostId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        reviewMapper.updateReviewFromDto(reviewDTO, review);

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toDTO(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
    }
}