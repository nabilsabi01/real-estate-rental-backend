package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.AmenityDTO;
import com.easystay.realestaterental.service.AmenityService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/amenities")
@RequiredArgsConstructor
@Tag(name = "Amenities", description = "Amenity management APIs")
public class AmenityController {
    private final AmenityService amenityService;

    @GetMapping
    @Operation(summary = "Get all amenities")
    public ResponseEntity<Page<AmenityDTO>> getAllAmenities(@PageableDefault(sort = "name") Pageable pageable) {
        Page<AmenityDTO> amenities = amenityService.getAllAmenities(pageable);
        return ResponseEntity.ok(amenities);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an amenity by ID")
    public ResponseEntity<AmenityDTO> getAmenityById(@PathVariable Long id) {
        AmenityDTO amenity = amenityService.getAmenityById(id);
        return ResponseEntity.ok(amenity);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new amenity")
    public ResponseEntity<AmenityDTO> createAmenity(@Valid @RequestBody AmenityDTO amenityDTO) {
        AmenityDTO createdAmenity = amenityService.createAmenity(amenityDTO);
        return new ResponseEntity<>(createdAmenity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing amenity")
    public ResponseEntity<AmenityDTO> updateAmenity(@PathVariable Long id, @Valid @RequestBody AmenityDTO amenityDTO) {
        AmenityDTO updatedAmenity = amenityService.updateAmenity(id, amenityDTO);
        return ResponseEntity.ok(updatedAmenity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an amenity")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search amenities by name")
    public ResponseEntity<Page<AmenityDTO>> searchAmenities(
            @RequestParam String name,
            @PageableDefault(sort = "name") Pageable pageable) {
        Page<AmenityDTO> amenities = amenityService.searchAmenities(name, pageable);
        return ResponseEntity.ok(amenities);
    }

    @GetMapping("/property/{propertyId}")
    @Operation(summary = "Get amenities for a specific property")
    public ResponseEntity<List<AmenityDTO>> getAmenitiesByPropertyId(@PathVariable Long propertyId) {
        List<AmenityDTO> amenities = amenityService.getAmenitiesByPropertyId(propertyId);
        return ResponseEntity.ok(amenities);
    }

    @GetMapping("/popular")
    @Operation(summary = "Get most popular amenities")
    public ResponseEntity<Page<AmenityDTO>> getMostPopularAmenities(@PageableDefault() Pageable pageable) {
        Page<AmenityDTO> popularAmenities = amenityService.getMostPopularAmenities(pageable);
        return ResponseEntity.ok(popularAmenities);
    }
}