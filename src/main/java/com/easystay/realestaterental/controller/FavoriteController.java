package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.FavoriteDTO;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing favorites.
 */
@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@Validated
@Tag(name = "Favorites", description = "Favorite management APIs")
public class FavoriteController {
    private final FavoriteService favoriteService;

    /**
     * Adds a property to the user's favorites.
     *
     * @param guest      the authenticated guest
     * @param propertyId the ID of the property to favorite
     * @return the created favorite DTO
     */
    @PostMapping("/{propertyId}")
    @Operation(summary = "Add a property to favorites")
    @ApiResponse(responseCode = "200", description = "Property added to favorites successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Property not found")
    public ResponseEntity<FavoriteDTO> addFavorite(
            @AuthenticationPrincipal Guest guest,
            @PathVariable @Positive Long propertyId) {
        FavoriteDTO favorite = favoriteService.addFavorite(guest.getId(), propertyId);
        return ResponseEntity.ok(favorite);
    }

    /**
     * Removes a property from the user's favorites.
     *
     * @param guest      the authenticated guest
     * @param propertyId the ID of the property to unfavorite
     * @return no content if successful
     */
    @DeleteMapping("/{propertyId}")
    @Operation(summary = "Remove a property from favorites")
    @ApiResponse(responseCode = "204", description = "Property removed from favorites successfully")
    @ApiResponse(responseCode = "404", description = "Favorite not found")
    public ResponseEntity<Void> removeFavorite(
            @AuthenticationPrincipal Guest guest,
            @PathVariable @Positive Long propertyId) {
        favoriteService.removeFavorite(guest.getId(), propertyId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Gets all favorite properties for the logged-in guest.
     *
     * @param guest    the authenticated guest
     * @return a page of favorite DTOs
     */
    @GetMapping
    @Operation(summary = "Get favorites for the logged-in guest")
    public ResponseEntity<Page<FavoriteDTO>> getFavorites(
            @AuthenticationPrincipal Guest guest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "favoritedAt,desc") String sort) {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        return ResponseEntity.ok(favoriteService.getFavoritesByGuestId(guest.getId(), pageRequest));
    }

    /**
     * Gets the number of times a property has been favorited.
     *
     * @param propertyId the ID of the property
     * @return the count of favorites for the property
     */
    @GetMapping("/count/{propertyId}")
    @Operation(summary = "Get the number of times a property has been favorited")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved favorite count")
    public ResponseEntity<Long> getFavoriteCount(
            @PathVariable @Positive Long propertyId) {
        long count = favoriteService.getFavoriteCount(propertyId);
        return ResponseEntity.ok(count);
    }

    /**
     * Checks if a property is in the user's favorites.
     *
     * @param guest      the authenticated guest
     * @param propertyId the ID of the property to check
     * @return true if the property is favorited, false otherwise
     */
    @GetMapping("/check/{propertyId}")
    @Operation(summary = "Check if a property is in favorites")
    @ApiResponse(responseCode = "200", description = "Successfully checked favorite status")
    public ResponseEntity<Boolean> isFavorite(
            @AuthenticationPrincipal Guest guest,
            @PathVariable @Positive Long propertyId) {
        boolean isFavorite = favoriteService.isFavorite(guest.getId(), propertyId);
        return ResponseEntity.ok(isFavorite);
    }
}