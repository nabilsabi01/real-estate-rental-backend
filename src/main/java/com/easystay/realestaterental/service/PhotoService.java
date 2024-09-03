package com.easystay.realestaterental.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.easystay.realestaterental.entity.Photo;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.exception.FileUploadException;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.repository.PhotoRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class PhotoService {

    @Value("${app.file.max-size}")
    private long maxFileSize;

    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png"};

    private final Cloudinary cloudinary;

    private final PhotoRepository photoRepository;

    private final PropertyRepository propertyRepository;

    public PhotoService(Cloudinary cloudinary, PhotoRepository photoRepository, PropertyRepository propertyRepository) {
        this.cloudinary = cloudinary;
        this.photoRepository = photoRepository;
        this.propertyRepository = propertyRepository;
    }

    @Transactional
    public Photo uploadPhoto(Long propertyId, MultipartFile file, String caption, boolean isPrimary) {
        try {
            validateFile(file);

            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            Photo photo = new Photo();
            photo.setProperty(property);
            photo.setUrl(imageUrl);
            photo.setCaption(caption);
            photo.setPrimaryPhoto(isPrimary);

            if (isPrimary) {
                updatePrimaryPhoto(propertyId);
            }

            return photoRepository.save(photo);
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file", e);
        }
    }

    @Transactional
    public void deletePhoto(Long propertyId, Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id: " + photoId));

        if (!photo.getProperty().getId().equals(propertyId)) {
            throw new ResourceNotFoundException("Photo not found for the given property");
        }

        try {
            String publicId = extractPublicIdFromUrl(photo.getUrl());
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            photoRepository.delete(photo);
        } catch (IOException e) {
            throw new FileUploadException("Failed to delete file from Cloudinary", e);
        }
    }

    public List<Photo> getPhotosByPropertyId(Long propertyId) {
        return photoRepository.findByPropertyId(propertyId);
    }

    @Transactional
    public Photo updatePhotoDetails(Long propertyId, Long photoId, String caption, Boolean isPrimary) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id: " + photoId));

        if (!photo.getProperty().getId().equals(propertyId)) {
            throw new ResourceNotFoundException("Photo not found for the given property");
        }

        if (caption != null) {
            photo.setCaption(caption);
        }

        if (isPrimary != null && isPrimary) {
            updatePrimaryPhoto(propertyId);
            photo.setPrimaryPhoto(true);
        }

        return photoRepository.save(photo);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileUploadException("Failed to upload empty file");
        }
        if (file.getSize() > maxFileSize) {
            throw new FileUploadException("File size exceeds maximum limit of " + (maxFileSize / 1024 / 1024) + "MB");
        }
        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!Arrays.asList(ALLOWED_EXTENSIONS).contains(fileExtension.toLowerCase())) {
            throw new FileUploadException("File type not allowed. Allowed types: " + String.join(", ", ALLOWED_EXTENSIONS));
        }
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private void updatePrimaryPhoto(Long propertyId) {
        photoRepository.findByPropertyIdAndPrimaryPhotoIsTrue(propertyId)
                .ifPresent(oldPrimary -> {
                    oldPrimary.setPrimaryPhoto(false);
                    photoRepository.save(oldPrimary);
                });
    }

    private String extractPublicIdFromUrl(String url) {
        String[] urlParts = url.split("/");
        String fileName = urlParts[urlParts.length - 1];
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
}