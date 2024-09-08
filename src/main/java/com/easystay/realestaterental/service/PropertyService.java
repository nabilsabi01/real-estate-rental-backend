package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.entity.Host;
import com.easystay.realestaterental.entity.Photo;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.PropertyMapper;
import com.easystay.realestaterental.repository.HostRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final HostRepository hostRepository;
    private final PropertyMapper propertyMapper;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public PropertyDTO createProperty(PropertyDTO propertyDTO, List<MultipartFile> photos) {
        Property property = propertyMapper.toEntity(propertyDTO);

        // Fetch and set the host
        Host host = hostRepository.findById(propertyDTO.getHostId())
                .orElseThrow(() -> new ResourceNotFoundException("Host not found with id: " + propertyDTO.getHostId()));
        property.setHost(host);

        List<Photo> uploadedPhotos = uploadPhotos(photos);
        property.setPhotos(uploadedPhotos);

        // Set the property for each photo
        uploadedPhotos.forEach(photo -> photo.setProperty(property));

        Property savedProperty = propertyRepository.save(property);
        return propertyMapper.toDTO(savedProperty);
    }

    public PropertyDTO getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        return propertyMapper.toDTO(property);
    }

    public List<PropertyDTO> getAllProperties() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PropertyDTO updateProperty(Long id, PropertyDTO propertyDTO) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        propertyMapper.updatePropertyFromDto(propertyDTO, property);

        // Update host if hostId has changed
        if (!property.getHost().getId().equals(propertyDTO.getHostId())) {
            Host newHost = hostRepository.findById(propertyDTO.getHostId())
                    .orElseThrow(() -> new ResourceNotFoundException("Host not found with id: " + propertyDTO.getHostId()));
            property.setHost(newHost);
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

    @Transactional
    public PropertyDTO addPhotosToProperty(Long id, List<MultipartFile> photos) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        List<Photo> uploadedPhotos = uploadPhotos(photos);
        uploadedPhotos.forEach(photo -> photo.setProperty(property));
        property.getPhotos().addAll(uploadedPhotos);

        Property updatedProperty = propertyRepository.save(property);
        return propertyMapper.toDTO(updatedProperty);
    }

    @Transactional
    public void deletePhotoFromProperty(Long propertyId, Long photoId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

        Photo photoToDelete = property.getPhotos().stream()
                .filter(photo -> photo.getId().equals(photoId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id: " + photoId));

        cloudinaryService.deleteImage(photoToDelete.getPhotoUrl());
        property.getPhotos().remove(photoToDelete);
        propertyRepository.save(property);
    }

    private List<Photo> uploadPhotos(List<MultipartFile> photos) {
        return photos.stream()
                .map(photo -> {
                    String url = cloudinaryService.uploadImage(photo);
                    Photo newPhoto = new Photo();
                    newPhoto.setPhotoUrl(url);
                    return newPhoto;
                })
                .collect(Collectors.toList());
    }
}