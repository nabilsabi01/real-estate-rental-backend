package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.PhotoDTO;
import com.easystay.realestaterental.entity.Photo;
import com.easystay.realestaterental.exception.FileUploadException;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.PhotoMapper;
import com.easystay.realestaterental.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/properties/{propertyId}/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoMapper photoMapper;

    @PostMapping
    public ResponseEntity<?> uploadPhoto(
            @PathVariable Long propertyId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String caption,
            @RequestParam(defaultValue = "false") boolean isPrimary) {
        try {
            Photo photo = photoService.uploadPhoto(propertyId, file, caption, isPrimary);
            return ResponseEntity.ok(photoMapper.toDTO(photo));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (FileUploadException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (MaxUploadSizeExceededException e) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeds the maximum allowed size");
        }
    }

    @GetMapping
    public ResponseEntity<List<PhotoDTO>> getPhotosByPropertyId(@PathVariable Long propertyId) {
        List<Photo> photos = photoService.getPhotosByPropertyId(propertyId);
        List<PhotoDTO> photoDTOs = photos.stream()
                .map(photoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(photoDTOs);
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<?> deletePhoto(
            @PathVariable Long propertyId,
            @PathVariable Long photoId) {
        try {
            photoService.deletePhoto(propertyId, photoId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (FileUploadException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{photoId}")
    public ResponseEntity<?> updatePhotoDetails(
            @PathVariable Long propertyId,
            @PathVariable Long photoId,
            @RequestParam(required = false) String caption,
            @RequestParam(required = false) Boolean isPrimary) {
        try {
            Photo updatedPhoto = photoService.updatePhotoDetails(propertyId, photoId, caption, isPrimary);
            return ResponseEntity.ok(photoMapper.toDTO(updatedPhoto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}