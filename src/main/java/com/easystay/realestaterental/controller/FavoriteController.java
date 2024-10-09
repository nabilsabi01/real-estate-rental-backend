package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.FavoriteDTO;
import com.easystay.realestaterental.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@Validated
@Tag(name = "Favorites", description = "Favorite management APIs")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/guests/{guestId}/properties/{propertyId}")
    @PreAuthorize("hasRole('GUEST') and #guestId == authentication.principal.id")
    @Operation(summary = "Add a property to favorites")
    public ResponseEntity<FavoriteDTO> addFavorite(
            @Parameter(description = "ID of the guest", required = true)
            @PathVariable @Positive Long guestId,
            @Parameter(description = "ID of the property", required = true)
            @PathVariable @Positive Long propertyId) {
        FavoriteDTO favoriteDTO = favoriteService.addFavorite(guestId, propertyId);
        return new ResponseEntity<>(favoriteDTO, HttpStatus.CREATED);
    }

    @GetMapping("/guests/{guestId}")
    @PreAuthorize("hasRole('GUEST') and #guestId == authentication.principal.id")
    @Operation(summary = "Get favorites for a guest")
    public ResponseEntity<Page<FavoriteDTO>> getFavoritesByGuestId(
            @Parameter(description = "ID of the guest", required = true)
            @PathVariable @Positive Long guestId,
            @PageableDefault(size = 20, sort = "favoritedAt") Pageable pageable) {
        Page<FavoriteDTO> favorites = favoriteService.getFavoritesByGuestId(guestId, pageable);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/guests/{guestId}/properties/{propertyId}")
    @PreAuthorize("hasRole('GUEST') and #guestId == authentication.principal.id")
    @Operation(summary = "Remove a property from favorites")
    public ResponseEntity<Void> removeFavorite(
            @Parameter(description = "ID of the guest", required = true)
            @PathVariable @Positive Long guestId,
            @Parameter(description = "ID of the property", required = true)
            @PathVariable @Positive Long propertyId) {
        favoriteService.removeFavorite(guestId, propertyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/guests/{guestId}/properties/{propertyId}")
    @PreAuthorize("hasRole('GUEST') and #guestId == authentication.principal.id")
    @Operation(summary = "Check if a property is in favorites")
    public ResponseEntity<Boolean> isFavorite(
            @Parameter(description = "ID of the guest", required = true)
            @PathVariable @Positive Long guestId,
            @Parameter(description = "ID of the property", required = true)
            @PathVariable @Positive Long propertyId) {
        boolean isFavorite = favoriteService.isFavorite(guestId, propertyId);
        return ResponseEntity.ok(isFavorite);
    }

    @GetMapping("/properties/{propertyId}/count")
    @Operation(summary = "Get the number of times a property has been favorited")
    public ResponseEntity<Long> getFavoriteCount(
            @Parameter(description = "ID of the property", required = true)
            @PathVariable @Positive Long propertyId) {
        long count = favoriteService.getFavoriteCount(propertyId);
        return ResponseEntity.ok(count);
    }
}