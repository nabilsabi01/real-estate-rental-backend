package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCities() {
        return ResponseEntity.ok(locationService.getAllCities());
    }

    @GetMapping("/countries")
    public ResponseEntity<List<String>> getAllCountries() {
        return ResponseEntity.ok(locationService.getAllCountries());
    }

    @GetMapping("/properties/city/{city}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByCity(@PathVariable String city) {
        return ResponseEntity.ok(locationService.getPropertiesByCity(city));
    }

    @GetMapping("/properties/country/{country}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByCountry(@PathVariable String country) {
        return ResponseEntity.ok(locationService.getPropertiesByCountry(country));
    }

    @GetMapping("/properties/area")
    public ResponseEntity<List<PropertyDTO>> getPropertiesWithinArea(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLon,
            @RequestParam double maxLon) {
        return ResponseEntity.ok(locationService.getPropertiesWithinArea(minLat, maxLat, minLon, maxLon));
    }
}