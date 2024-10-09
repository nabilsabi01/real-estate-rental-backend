package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.ReviewDTO;
import com.easystay.realestaterental.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Review management APIs")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('GUEST')")
    @Operation(summary = "Create a new review")
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a review by ID")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/property/{propertyId}")
    @Operation(summary = "Get reviews for a property")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByPropertyId(
            @PathVariable Long propertyId,
            @PageableDefault(sort = "createdAt") Pageable pageable) {
        Page<ReviewDTO> reviews = reviewService.getReviewsByPropertyId(propertyId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasRole('GUEST') and #guestId == authentication.principal.id")
    @Operation(summary = "Get reviews by a guest")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByGuestId(
            @PathVariable Long guestId,
            @PageableDefault(sort = "createdAt") Pageable pageable) {
        Page<ReviewDTO> reviews = reviewService.getReviewsByGuestId(guestId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/host/{hostId}")
    @PreAuthorize("hasRole('HOST') and #hostId == authentication.principal.id")
    @Operation(summary = "Get reviews for a host's properties")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByHostId(
            @PathVariable Long hostId,
            @PageableDefault(sort = "createdAt") Pageable pageable) {
        Page<ReviewDTO> reviews = reviewService.getReviewsByHostId(hostId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GUEST') and @reviewService.isReviewOwner(#id, authentication.principal.id)")
    @Operation(summary = "Update a review")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GUEST') and @reviewService.isReviewOwner(#id, authentication.principal.id)")
    @Operation(summary = "Delete a review")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}