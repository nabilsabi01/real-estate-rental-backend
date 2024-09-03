package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.AmenityDTO;
import com.easystay.realestaterental.entity.Amenity;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.AmenityMapper;
import com.easystay.realestaterental.repository.AmenityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;
    private final AmenityMapper amenityMapper;

    @Autowired
    public AmenityService(AmenityRepository amenityRepository, AmenityMapper amenityMapper) {
        this.amenityRepository = amenityRepository;
        this.amenityMapper = amenityMapper;
    }

    public List<AmenityDTO> getAllAmenities() {
        return amenityRepository.findAll().stream()
                .map(amenityMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AmenityDTO getAmenityById(Long id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));
        return amenityMapper.toDTO(amenity);
    }

    public AmenityDTO createAmenity(AmenityDTO amenityDTO) {
        Amenity amenity = amenityMapper.toEntity(amenityDTO);
        Amenity savedAmenity = amenityRepository.save(amenity);
        return amenityMapper.toDTO(savedAmenity);
    }

    public AmenityDTO updateAmenity(Long id, AmenityDTO amenityDTO) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));

        amenity.setName(amenityDTO.getName());
        amenity.setIcon(amenityDTO.getIcon());

        Amenity updatedAmenity = amenityRepository.save(amenity);
        return amenityMapper.toDTO(updatedAmenity);
    }

    public void deleteAmenity(Long id) {
        if (!amenityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Amenity not found with id: " + id);
        }
        amenityRepository.deleteById(id);
    }
}