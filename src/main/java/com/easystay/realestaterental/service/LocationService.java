package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.mapper.PropertyMapper;
import com.easystay.realestaterental.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final PropertyMapper propertyMapper;

    @Autowired
    public LocationService(LocationRepository locationRepository, PropertyMapper propertyMapper) {
        this.locationRepository = locationRepository;
        this.propertyMapper = propertyMapper;
    }

    public List<String> getAllCities() {
        return locationRepository.findAllCities();
    }

    public List<String> getAllCountries() {
        return locationRepository.findAllCountries();
    }

    public List<PropertyDTO> getPropertiesByCity(String city) {
        return locationRepository.findPropertiesByCity(city).stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PropertyDTO> getPropertiesByCountry(String country) {
        return locationRepository.findPropertiesByCountry(country).stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PropertyDTO> getPropertiesWithinArea(double minLat, double maxLat, double minLon, double maxLon) {
        return locationRepository.findPropertiesWithinArea(minLat, maxLat, minLon, maxLon).stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());
    }
}