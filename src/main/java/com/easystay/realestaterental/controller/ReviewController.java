package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.entity.Review;
import com.easystay.realestaterental.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<Review>> getReviewsByPropertyId(@PathVariable Long propertyId) {
        List<Review> reviews = reviewService.getReviewsByPropertyId(propertyId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Review>> getReviewsByGuestId(@PathVariable Long guestId) {
        List<Review> reviews = reviewService.getReviewsByGuestId(guestId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<Review>> getReviewsByHostId(@PathVariable Long hostId) {
        List<Review> reviews = reviewService.getReviewsByHostId(hostId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/property/{propertyId}/guest/{guestId}")
    public ResponseEntity<Review> createReview(
            @PathVariable Long propertyId,
            @PathVariable Long guestId,
            @RequestParam int rating,
            @RequestParam String comment) {
        Review review = reviewService.createReview(propertyId, guestId, rating, comment);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestParam int rating,
            @RequestParam String comment) {
        Review review = reviewService.updateReview(reviewId, rating, comment);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}