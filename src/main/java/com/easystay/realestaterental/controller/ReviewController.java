package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.ReviewDTO;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Review management APIs")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/properties/{propertyId}")
    @Operation(summary = "Add a review to a property")
    @ApiResponse(responseCode = "201", description = "Review created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Property not found")
    public ResponseEntity<ReviewDTO> addReview(
            @AuthenticationPrincipal Guest guest,
            @PathVariable Long propertyId,
            @Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.addReview(guest.getId(), propertyId, reviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/properties/{propertyId}")
    @Operation(summary = "Get reviews for a property")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews")
    public ResponseEntity<Page<ReviewDTO>> getReviewsForProperty(
            @PathVariable Long propertyId,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<ReviewDTO> reviews = reviewService.getReviewsForProperty(propertyId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "Update a review")
    @ApiResponse(responseCode = "200", description = "Review updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "403", description = "Not authorized to update this review")
    @ApiResponse(responseCode = "404", description = "Review not found")
    public ResponseEntity<ReviewDTO> updateReview(
            @AuthenticationPrincipal Guest guest,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(guest.getId(), reviewId, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete a review")
    @ApiResponse(responseCode = "204", description = "Review deleted successfully")
    @ApiResponse(responseCode = "403", description = "Not authorized to delete this review")
    @ApiResponse(responseCode = "404", description = "Review not found")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal Guest guest,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(guest.getId(), reviewId);
        return ResponseEntity.noContent().build();
    }
}