package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.FavoriteDTO;
import com.easystay.realestaterental.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@Validated
@Tag(name = "Favorites", description = "Favorite management APIs")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping(path = "/guests/{guestId}/properties/{propertyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('GUEST') and #guestId == authentication.principal.id")
    @Operation(summary = "Add a property to favorites",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Property added to favorites successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Guest or Property not found"),
                    @ApiResponse(responseCode = "409", description = "Property is already in favorites")
            })
    public ResponseEntity<FavoriteDTO> addFavorite(
            @Parameter(description = "ID of the guest", required = true) @PathVariable @Positive Long guestId,
            @Parameter(description = "ID of the property", required = true) @PathVariable @Positive Long propertyId) {
        FavoriteDTO favoriteDTO = favoriteService.addFavorite(guestId, propertyId);
        return ResponseEntity
                .created(URI.create("/api/v1/favorites/" + favoriteDTO.getId()))
                .body(favoriteDTO);
    }

    @GetMapping(path = "/guests/{guestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('GUEST') and #guestId == authentication.principal.id")
    @Operation(summary = "Get favorites for a guest",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Favorites retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Guest not found")
            })
    public ResponseEntity<Page<FavoriteDTO>> getFavoritesByGuestId(
            @Parameter(description = "ID of the guest", required = true) @PathVariable @Positive Long guestId,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<FavoriteDTO> favorites = favoriteService.getFavoritesByGuestId(guestId, pageable);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/guests/{guestId}/properties/{propertyId}")
    @PreAuthorize("hasRole('GUEST') and #guestId == authentication.principal.id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a property from favorites",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Property removed from favorites successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Favorite not found")
            })
    public void removeFavorite(
            @Parameter(description = "ID of the guest", required = true) @PathVariable @Positive Long guestId,
            @Parameter(description = "ID of the property", required = true) @PathVariable @Positive Long propertyId) {
        favoriteService.removeFavorite(guestId, propertyId);
    }

    @GetMapping(path = "/guests/{guestId}/properties/{propertyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('GUEST') and #guestId == authentication.principal.id")
    @Operation(summary = "Check if a property is in favorites",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Checked successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<Boolean> isFavorite(
            @Parameter(description = "ID of the guest", required = true) @PathVariable @Positive Long guestId,
            @Parameter(description = "ID of the property", required = true) @PathVariable @Positive Long propertyId) {
        boolean isFavorite = favoriteService.isFavorite(guestId, propertyId);
        return ResponseEntity.ok(isFavorite);
    }

    @GetMapping(path = "/properties/{propertyId}/count", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get the number of times a property has been favorited",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Property not found")
            })
    public ResponseEntity<Long> getFavoriteCount(
            @Parameter(description = "ID of the property", required = true) @PathVariable @Positive Long propertyId) {
        long count = favoriteService.getFavoriteCount(propertyId);
        return ResponseEntity.ok(count);
    }
}