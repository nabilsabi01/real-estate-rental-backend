package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
@Tag(name = "Properties", description = "Property management APIs")
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('HOST')")
    @Operation(summary = "Create a new property")
    public ResponseEntity<PropertyDTO> createProperty(
            @RequestPart("property") @Valid PropertyDTO propertyDTO,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {
        PropertyDTO createdProperty = propertyService.createProperty(propertyDTO, photos);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a property by ID")
    public ResponseEntity<PropertyDTO> getProperty(@PathVariable Long id, @RequestParam(required = false) Long guestId) {
        PropertyDTO property = propertyService.getPropertyById(id, guestId);
        return ResponseEntity.ok(property);
    }

    @GetMapping
    @Operation(summary = "Get all properties")
    public ResponseEntity<Page<PropertyDTO>> getAllProperties(Pageable pageable, @RequestParam(required = false) Long guestId) {
        Page<PropertyDTO> properties = propertyService.getAllProperties(pageable, guestId);
        return ResponseEntity.ok(properties);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('HOST') and @propertyService.isPropertyOwner(#id, authentication.principal.id)")
    @Operation(summary = "Update a property")
    public ResponseEntity<PropertyDTO> updateProperty(
            @PathVariable Long id,
            @RequestPart("property") @Valid PropertyDTO propertyDTO,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {
        PropertyDTO updatedProperty = propertyService.updateProperty(id, propertyDTO, photos);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOST') and @propertyService.isPropertyOwner(#id, authentication.principal.id)")
    @Operation(summary = "Delete a property")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured properties")
    public ResponseEntity<List<PropertyDTO>> getFeaturedProperties(@RequestParam(required = false) Long guestId) {
        List<PropertyDTO> featuredProperties = propertyService.getFeaturedProperties(guestId);
        return ResponseEntity.ok(featuredProperties);
    }
}