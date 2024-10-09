package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.entity.Amenity;
import com.easystay.realestaterental.entity.Host;
import com.easystay.realestaterental.entity.Photo;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.PropertyMapper;
import com.easystay.realestaterental.repository.AmenityRepository;
import com.easystay.realestaterental.repository.HostRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final FavoriteService favoriteService;
    private final CloudinaryService cloudinaryService;
    private final HostRepository hostRepository;
    private final AmenityRepository amenityRepository;

    @Transactional
    public PropertyDTO createProperty(PropertyDTO propertyDTO, List<MultipartFile> photoFiles) {
        Property property = propertyMapper.toEntity(propertyDTO);

        // Set the host
        Host host = hostRepository.findById(propertyDTO.getHostId())
                .orElseThrow(() -> new ResourceNotFoundException("Host not found with id: " + propertyDTO.getHostId()));
        property.setHost(host);

        // Set amenities
        if (propertyDTO.getAmenityIds() != null && !propertyDTO.getAmenityIds().isEmpty()) {
            Set<Amenity> amenities = propertyDTO.getAmenityIds().stream()
                    .map(amenityId -> amenityRepository.findById(amenityId)
                            .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + amenityId)))
                    .collect(Collectors.toSet());
            property.setAmenities(amenities);
        }

        // Upload and set photos
        if (photoFiles != null && !photoFiles.isEmpty()) {
            List<Photo> photos = uploadPhotos(photoFiles, property);
            property.setPhotos(photos);
        }

        Property savedProperty = propertyRepository.save(property);
        return propertyMapper.toDTO(savedProperty);
    }

    private List<Photo> uploadPhotos(List<MultipartFile> photoFiles, Property property) {
        return photoFiles.stream().map(file -> {
            String url = cloudinaryService.uploadImage(file);
            Photo photo = new Photo();
            photo.setPhotoUrl(url);
            photo.setProperty(property);
            return photo;
        }).collect(Collectors.toList());
    }

    public PropertyDTO getPropertyById(Long id, Long guestId) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        PropertyDTO propertyDTO = propertyMapper.toDTO(property);
        propertyDTO.setIsFavorited(favoriteService.isFavorite(guestId, id));
        return propertyDTO;
    }

    public Page<PropertyDTO> getAllProperties(Pageable pageable, Long guestId) {
        return propertyRepository.findAll(pageable)
                .map(property -> {
                    PropertyDTO dto = propertyMapper.toDTO(property);
                    dto.setIsFavorited(favoriteService.isFavorite(guestId, property.getId()));
                    return dto;
                });
    }

    @Transactional
    public PropertyDTO updateProperty(Long id, PropertyDTO propertyDTO, List<MultipartFile> newPhotoFiles) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        propertyMapper.updatePropertyFromDto(propertyDTO, property);

        if (newPhotoFiles != null && !newPhotoFiles.isEmpty()) {
            List<Photo> newPhotos = uploadPhotos(newPhotoFiles, property);
            property.getPhotos().addAll(newPhotos);
        }

        Property updatedProperty = propertyRepository.save(property);
        return propertyMapper.toDTO(updatedProperty);
    }

    @Transactional
    public void deleteProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        // Delete photos from Cloudinary
        for (Photo photo : property.getPhotos()) {
            cloudinaryService.deleteImage(photo.getPhotoUrl());
        }

        propertyRepository.delete(property);
    }

    public List<PropertyDTO> getFeaturedProperties(Long guestId) {
        List<Property> featuredProperties = propertyRepository.findTop10ByOrderByCreatedAtDesc();
        return featuredProperties.stream()
                .map(property -> {
                    PropertyDTO dto = propertyMapper.toDTO(property);
                    dto.setIsFavorited(favoriteService.isFavorite(guestId, property.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public boolean isPropertyOwner(Long propertyId, Long hostId) {
        return propertyRepository.findById(propertyId)
                .map(property -> property.getHost().getId().equals(hostId))
                .orElse(false);
    }
}