package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.entity.*;
import com.easystay.realestaterental.enums.PropertyType;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.PropertyMapper;
import com.easystay.realestaterental.repository.AmenityRepository;
import com.easystay.realestaterental.repository.HostRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private PropertyMapper propertyMapper;
    @Mock
    private FavoriteService favoriteService;
    @Mock
    private CloudinaryService cloudinaryService;
    @Mock
    private HostRepository hostRepository;
    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private PropertyService propertyService;

    private PropertyDTO propertyDTO;
    private Property property;
    private Host host;
    private Amenity amenity;

    @BeforeEach
    void setUp() {
        host = new Host();
        host.setId(1L);

        amenity = new Amenity();
        amenity.setId(1L);
        amenity.setName("WiFi");

        property = new Property();
        property.setId(1L);
        property.setTitle("Test Property");
        property.setDescription("A nice property for testing");
        property.setPricePerNight(new BigDecimal("100.00"));
        property.setMaxGuests(4);
        property.setBedrooms(2);
        property.setBeds(2);
        property.setBathrooms(1);
        property.setPropertyType(PropertyType.APARTMENT);
        property.setHost(host);
        property.setAmenities(new HashSet<>(Collections.singletonList(amenity)));

        Location location = new Location();
        location.setCountry("Test Country");
        location.setCity("Test City");
        property.setLocation(location);

        propertyDTO = new PropertyDTO();
        propertyDTO.setId(1L);
        propertyDTO.setTitle("Test Property");
        propertyDTO.setHostId(1L);
        propertyDTO.setAmenityIds(Collections.singleton(1L));
    }

    @Test
    void createProperty_ShouldReturnCreatedPropertyDTO() {
        List<MultipartFile> photoFiles = new ArrayList<>();
        when(hostRepository.findById(anyLong())).thenReturn(Optional.of(host));
        when(amenityRepository.findById(anyLong())).thenReturn(Optional.of(amenity));
        when(propertyMapper.toEntity(any(PropertyDTO.class))).thenReturn(property);
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(propertyMapper.toDTO(any(Property.class))).thenReturn(propertyDTO);

        PropertyDTO result = propertyService.createProperty(propertyDTO, photoFiles);

        assertNotNull(result);
        assertEquals(propertyDTO, result);
        verify(hostRepository).findById(1L);
        verify(amenityRepository).findById(1L);
        verify(propertyRepository).save(property);
    }

    @Test
    void getPropertyById_WithValidId_ShouldReturnPropertyDTO() {
        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(propertyMapper.toDTO(any(Property.class))).thenReturn(propertyDTO);
        when(favoriteService.isFavorite(anyLong(), anyLong())).thenReturn(true);

        PropertyDTO result = propertyService.getPropertyById(1L, 1L);

        assertNotNull(result);
        assertEquals(propertyDTO, result);
        assertTrue(result.getIsFavorited());
    }

    @Test
    void getAllProperties_ShouldReturnPageOfPropertyDTOs() {
        Pageable pageable = Pageable.unpaged();
        Page<Property> propertyPage = new PageImpl<>(Collections.singletonList(property));
        when(propertyRepository.findAll(any(Pageable.class))).thenReturn(propertyPage);
        when(propertyMapper.toDTO(any(Property.class))).thenReturn(propertyDTO);
        when(favoriteService.isFavorite(anyLong(), anyLong())).thenReturn(false);

        Page<PropertyDTO> result = propertyService.getAllProperties(pageable, 1L);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertFalse(result.getContent().get(0).getIsFavorited());
    }

    @Test
    void updateProperty_WithValidId_ShouldReturnUpdatedPropertyDTO() {
        List<MultipartFile> newPhotoFiles = new ArrayList<>();
        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(propertyMapper.toDTO(any(Property.class))).thenReturn(propertyDTO);

        PropertyDTO result = propertyService.updateProperty(1L, propertyDTO, newPhotoFiles);

        assertNotNull(result);
        assertEquals(propertyDTO, result);
        verify(propertyMapper).updatePropertyFromDto(propertyDTO, property);
    }

    @Test
    void deleteProperty_WithValidId_ShouldDeleteProperty() {
        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));

        assertDoesNotThrow(() -> propertyService.deleteProperty(1L));
        verify(propertyRepository).delete(property);
        verify(cloudinaryService, times(property.getPhotos().size())).deleteImage(anyString());
    }

    @Test
    void getFeaturedProperties_ShouldReturnListOfPropertyDTOs() {
        List<Property> featuredProperties = Collections.singletonList(property);
        when(propertyRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(featuredProperties);
        when(propertyMapper.toDTO(any(Property.class))).thenReturn(propertyDTO);
        when(favoriteService.isFavorite(anyLong(), anyLong())).thenReturn(false);

        List<PropertyDTO> result = propertyService.getFeaturedProperties(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).getIsFavorited());
    }

    @Test
    void isPropertyOwner_WithValidIdAndOwner_ShouldReturnTrue() {
        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));

        boolean result = propertyService.isPropertyOwner(1L, 1L);

        assertTrue(result);
    }

    @Test
    void createProperty_WithInvalidHostId_ShouldThrowResourceNotFoundException() {
        when(hostRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(propertyMapper.toEntity(any(PropertyDTO.class))).thenReturn(new Property());

        assertThrows(ResourceNotFoundException.class, () -> propertyService.createProperty(propertyDTO, new ArrayList<>()));
    }

    @Test
    void createProperty_WithInvalidAmenityId_ShouldThrowResourceNotFoundException() {
        when(hostRepository.findById(anyLong())).thenReturn(Optional.of(host));
        when(amenityRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(propertyMapper.toEntity(any(PropertyDTO.class))).thenReturn(new Property());

        propertyDTO.setAmenityIds(Set.of(999L)); // ID invalide

        assertThrows(ResourceNotFoundException.class, () -> propertyService.createProperty(propertyDTO, new ArrayList<>()));

        verify(hostRepository).findById(1L);
        verify(amenityRepository).findById(999L);
    }
}