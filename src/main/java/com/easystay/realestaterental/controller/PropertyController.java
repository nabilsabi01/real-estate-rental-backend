package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.enums.PropertyType;
import com.easystay.realestaterental.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        List<PropertyDTO> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> getPropertyById(@PathVariable Long id) {
        PropertyDTO property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(property);
    }

    @PostMapping
    public ResponseEntity<PropertyDTO> createProperty(@RequestBody PropertyDTO propertyDTO) {
        PropertyDTO createdProperty = propertyService.createProperty(propertyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProperty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(@PathVariable Long id, @RequestBody PropertyDTO propertyDTO) {
        PropertyDTO updatedProperty = propertyService.updateProperty(id, propertyDTO);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByHostId(@PathVariable Long hostId) {
        List<PropertyDTO> properties = propertyService.getPropertiesByHostId(hostId);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/type/{propertyType}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByType(@PathVariable PropertyType propertyType) {
        List<PropertyDTO> properties = propertyService.getPropertiesByType(propertyType);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<PropertyDTO> properties = propertyService.getPropertiesByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByCity(@PathVariable String city) {
        List<PropertyDTO> properties = propertyService.getPropertiesByCity(city);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByCountry(@PathVariable String country) {
        List<PropertyDTO> properties = propertyService.getPropertiesByCountry(country);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/postalcode/{postalCode}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByPostalCode(@PathVariable String postalCode) {
        List<PropertyDTO> properties = propertyService.getPropertiesByPostalCode(postalCode);
        return ResponseEntity.ok(properties);
    }
}