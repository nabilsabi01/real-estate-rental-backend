package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.enums.PropertyType;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.PropertyMapper;
import com.easystay.realestaterental.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    public List<PropertyDTO> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PropertyDTO getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        return propertyMapper.toDTO(property);
    }

    public PropertyDTO createProperty(PropertyDTO propertyDTO) {
        Property property = propertyMapper.toEntity(propertyDTO);
        Property savedProperty = propertyRepository.save(property);
        return propertyMapper.toDTO(savedProperty);
    }

    public PropertyDTO updateProperty(Long id, PropertyDTO propertyDTO) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        property.setTitle(propertyDTO.getTitle());
        property.setDescription(propertyDTO.getDescription());
        property.setPricePerNight(propertyDTO.getPricePerNight());
        property.setMaxGuests(propertyDTO.getMaxGuests());
        property.setBedrooms(propertyDTO.getBedrooms());
        property.setBeds(propertyDTO.getBeds());
        property.setBathrooms(propertyDTO.getBathrooms());
        property.setPropertyType(propertyDTO.getPropertyType());

        Property updatedProperty = propertyRepository.save(property);
        return propertyMapper.toDTO(updatedProperty);
    }

    public void deleteProperty(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Property not found with id: " + id);
        }
        propertyRepository.deleteById(id);
    }

    public List<PropertyDTO> getPropertiesByHostId(Long hostId) {
        return propertyRepository.findByHostId(hostId).stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PropertyDTO> getPropertiesByType(PropertyType propertyType) {
        return propertyRepository.findByPropertyType(propertyType).stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PropertyDTO> getPropertiesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return propertyRepository.findByPricePerNightBetween(minPrice, maxPrice).stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PropertyDTO> getPropertiesByCity(String city) {
        return propertyRepository.findByCity(city).stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PropertyDTO> getPropertiesByCountry(String country) {
        return propertyRepository.findByCountry(country).stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PropertyDTO> getPropertiesByPostalCode(String postalCode) {
        return propertyRepository.findByPostalCode(postalCode).stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());
    }
}