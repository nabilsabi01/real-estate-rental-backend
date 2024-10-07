package com.easystay.realestaterental.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.entity.Host;
import com.easystay.realestaterental.entity.Photo;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.PropertyMapper;
import com.easystay.realestaterental.repository.PropertyRepository;
import com.easystay.realestaterental.repository.HostRepository;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private HostRepository hostRepository;

    @Mock
    private PropertyMapper propertyMapper;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private PropertyService propertyService;

    private Property property;
    private PropertyDTO propertyDTO;
    private Host host;

    @BeforeEach
    void setUp() {
        host = new Host();
        host.setId(1L);

        property = new Property();
        property.setId(1L);
        property.setTitle("Test Property");
        property.setHost(host);

        propertyDTO = new PropertyDTO();
        propertyDTO.setId(1L);
        propertyDTO.setTitle("Test Property");
        propertyDTO.setHostId(1L);
    }

    @Test
    void testCreateProperty() {
        when(hostRepository.findById(1L)).thenReturn(Optional.of(host));
        when(propertyMapper.toEntity(propertyDTO)).thenReturn(property);
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(propertyMapper.toDTO(property)).thenReturn(propertyDTO);

        PropertyDTO result = propertyService.createProperty(propertyDTO, new ArrayList<>());

        assertNotNull(result);
        assertEquals("Test Property", result.getTitle());
        verify(propertyRepository).save(any(Property.class));
    }

    @Test
    void testGetPropertyById() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(propertyMapper.toDTO(property)).thenReturn(propertyDTO);

        PropertyDTO result = propertyService.getPropertyById(1L);

        assertNotNull(result);
        assertEquals("Test Property", result.getTitle());
    }

    @Test
    void testGetPropertyById_NotFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> propertyService.getPropertyById(1L));
    }

    @Test
    void testGetAllProperties() {
        when(propertyRepository.findAll()).thenReturn(List.of(property));
        when(propertyMapper.toDTO(property)).thenReturn(propertyDTO);

        List<PropertyDTO> result = propertyService.getAllProperties();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Property", result.get(0).getTitle());
    }

    @Test
    void testUpdateProperty() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(propertyMapper.toDTO(property)).thenReturn(propertyDTO);

        PropertyDTO result = propertyService.updateProperty(1L, propertyDTO);

        assertNotNull(result);
        assertEquals("Test Property", result.getTitle());
        verify(propertyRepository).save(any(Property.class));
    }

    @Test
    void testDeleteProperty() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        propertyService.deleteProperty(1L);

        verify(propertyRepository).delete(property);
    }

    @Test
    void testAddPhotosToProperty() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(cloudinaryService.uploadImage(any())).thenReturn("photo_url");
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(propertyMapper.toDTO(property)).thenReturn(propertyDTO);

        List<MultipartFile> mockPhotos = List.of(mock(MultipartFile.class));
        PropertyDTO result = propertyService.addPhotosToProperty(1L, mockPhotos);

        assertNotNull(result);
        verify(cloudinaryService, times(mockPhotos.size())).uploadImage(any());
        verify(propertyRepository).save(property);
    }

    @Test
    void testDeletePhotoFromProperty() {
        Photo photo = new Photo();
        photo.setId(1L);
        photo.setPhotoUrl("photo_url");
        property.setPhotos(new ArrayList<>(List.of(photo)));

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        propertyService.deletePhotoFromProperty(1L, 1L);

        verify(cloudinaryService).deleteImage("photo_url");
        verify(propertyRepository).save(property);
        assertTrue(property.getPhotos().isEmpty());
    }
}