package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.AmenityDTO;
import com.easystay.realestaterental.service.AmenityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;

    @Autowired
    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public ResponseEntity<List<AmenityDTO>> getAllAmenities() {
        List<AmenityDTO> amenities = amenityService.getAllAmenities();
        return ResponseEntity.ok(amenities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityDTO> getAmenityById(@PathVariable Long id) {
        AmenityDTO amenity = amenityService.getAmenityById(id);
        return ResponseEntity.ok(amenity);
    }

    @PostMapping
    public ResponseEntity<AmenityDTO> createAmenity(@RequestBody AmenityDTO amenityDTO) {
        AmenityDTO createdAmenity = amenityService.createAmenity(amenityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAmenity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmenityDTO> updateAmenity(@PathVariable Long id, @RequestBody AmenityDTO amenityDTO) {
        AmenityDTO updatedAmenity = amenityService.updateAmenity(id, amenityDTO);
        return ResponseEntity.ok(updatedAmenity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return ResponseEntity.noContent().build();
    }
}