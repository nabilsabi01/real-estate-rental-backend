package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.ReviewDTO;
import com.easystay.realestaterental.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByPropertyId(@PathVariable Long propertyId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByPropertyId(propertyId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByGuestId(@PathVariable Long guestId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByGuestId(guestId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByHostId(@PathVariable Long hostId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByHostId(hostId);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long reviewId, @Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(reviewId, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}