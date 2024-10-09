package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.AmenityDTO;
import com.easystay.realestaterental.entity.Amenity;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.AmenityMapper;
import com.easystay.realestaterental.repository.AmenityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AmenityService {
    private final AmenityRepository amenityRepository;
    private final AmenityMapper amenityMapper;

    public Page<AmenityDTO> getAllAmenities(Pageable pageable) {
        return amenityRepository.findAll(pageable)
                .map(amenityMapper::toDTO);
    }

    public AmenityDTO getAmenityById(Long id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));
        return amenityMapper.toDTO(amenity);
    }

    @Transactional
    public AmenityDTO createAmenity(AmenityDTO amenityDTO) {
        Amenity amenity = amenityMapper.toEntity(amenityDTO);
        Amenity savedAmenity = amenityRepository.save(amenity);
        return amenityMapper.toDTO(savedAmenity);
    }

    @Transactional
    public AmenityDTO updateAmenity(Long id, AmenityDTO amenityDTO) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));

        amenityMapper.updateAmenityFromDto(amenityDTO, amenity);
        Amenity updatedAmenity = amenityRepository.save(amenity);
        return amenityMapper.toDTO(updatedAmenity);
    }

    @Transactional
    public void deleteAmenity(Long id) {
        if (!amenityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Amenity not found with id: " + id);
        }
        amenityRepository.deleteById(id);
    }

    public Page<AmenityDTO> searchAmenities(String name, Pageable pageable) {
        return amenityRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(amenityMapper::toDTO);
    }

    public List<AmenityDTO> getAmenitiesByPropertyId(Long propertyId) {
        return amenityRepository.findByPropertyId(propertyId).stream()
                .map(amenityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<AmenityDTO> getMostPopularAmenities(Pageable pageable) {
        return amenityRepository.findMostPopularAmenities(pageable)
                .map(amenityMapper::toDTO);
    }
}