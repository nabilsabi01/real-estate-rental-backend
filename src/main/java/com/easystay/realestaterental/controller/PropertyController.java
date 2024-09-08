package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.service.CloudinaryService;
import com.easystay.realestaterental.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;
    private final CloudinaryService cloudinaryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PropertyDTO> createProperty(
            @RequestPart("property") @Valid PropertyDTO propertyDTO,
            @RequestPart("photos") List<MultipartFile> photos) {
        PropertyDTO createdProperty = propertyService.createProperty(propertyDTO, photos);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> getProperty(@PathVariable Long id) {
        PropertyDTO property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(property);
    }

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        List<PropertyDTO> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(
            @PathVariable Long id,
            @RequestBody @Valid PropertyDTO propertyDTO) {
        PropertyDTO updatedProperty = propertyService.updateProperty(id, propertyDTO);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/photos")
    public ResponseEntity<PropertyDTO> addPhotosToProperty(
            @PathVariable Long id,
            @RequestPart("photos") List<MultipartFile> photos) {
        PropertyDTO updatedProperty = propertyService.addPhotosToProperty(id, photos);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{propertyId}/photos/{photoId}")
    public ResponseEntity<Void> deletePhotoFromProperty(
            @PathVariable Long propertyId,
            @PathVariable Long photoId) {
        propertyService.deletePhotoFromProperty(propertyId, photoId);
        return ResponseEntity.noContent().build();
    }
}