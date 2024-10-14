package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.FavoriteDTO;
import com.easystay.realestaterental.dto.PropertyDTO;
import com.easystay.realestaterental.entity.Favorite;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.FavoriteMapper;
import com.easystay.realestaterental.mapper.PropertyMapper;
import com.easystay.realestaterental.repository.FavoriteRepository;
import com.easystay.realestaterental.repository.GuestRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private FavoriteMapper favoriteMapper;

    @Mock
    private PropertyMapper propertyMapper;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    void addFavorite_ShouldReturnFavoriteDTO() {
        // Given
        Long guestId = 1L;
        Long propertyId = 1L;

        Guest guest = new Guest();
        guest.setId(guestId);

        Property property = new Property();
        property.setId(propertyId);

        Favorite favorite = new Favorite();
        favorite.setGuest(guest);
        favorite.setProperty(property);

        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setId(1L);

        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setId(propertyId);
        propertyDTO.setIsFavorited(true);

        // Mock repository and mapper responses
        when(guestRepository.findById(guestId)).thenReturn(Optional.of(guest));
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
        when(favoriteRepository.existsByGuestIdAndPropertyId(guestId, propertyId)).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);
        when(favoriteMapper.toDTO(favorite)).thenReturn(favoriteDTO);
        when(propertyMapper.toDTO(property)).thenReturn(propertyDTO);

        // When
        FavoriteDTO result = favoriteService.addFavorite(guestId, propertyId);

        // Then
        assertNotNull(result);
        assertNotNull(result.getProperty());
        assertEquals(propertyId, result.getProperty().getId());
        assertTrue(result.getProperty().getIsFavorited());
    }

    @Test
    void addFavorite_ShouldThrowException_WhenGuestNotFound() {
        // Given
        Long guestId = 1L;
        Long propertyId = 1L;

        when(guestRepository.findById(guestId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.addFavorite(guestId, propertyId));
    }

    @Test
    void addFavorite_ShouldThrowException_WhenPropertyNotFound() {
        // Given
        Long guestId = 1L;
        Long propertyId = 1L;

        Guest guest = new Guest();
        guest.setId(guestId);

        when(guestRepository.findById(guestId)).thenReturn(Optional.of(guest));
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.addFavorite(guestId, propertyId));
    }

    @Test
    void removeFavorite_ShouldDeleteFavorite() {
        // Given
        Long guestId = 1L;
        Long propertyId = 1L;

        // When
        favoriteService.removeFavorite(guestId, propertyId);

        // Then
        verify(favoriteRepository, times(1)).deleteByGuestIdAndPropertyId(guestId, propertyId);
    }

    @Test
    void getFavoritesByGuestId_ShouldReturnPageOfFavoriteDTOs() {
        // Given
        Long guestId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Property property = new Property();
        property.setId(1L);

        Favorite favorite = new Favorite();
        favorite.setId(1L);
        favorite.setProperty(property);

        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setId(1L);

        Page<Favorite> favoritePage = new PageImpl<>(Collections.singletonList(favorite), pageable, 1);

        when(favoriteRepository.findByGuestId(guestId, pageable)).thenReturn(favoritePage);
        when(favoriteMapper.toDTO(favorite)).thenReturn(favoriteDTO);

        // When
        Page<FavoriteDTO> result = favoriteService.getFavoritesByGuestId(guestId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(favoriteDTO.getId(), result.getContent().get(0).getId());
    }


    @Test
    void isFavorite_ShouldReturnTrue_WhenFavoriteExists() {
        // Given
        Long guestId = 1L;
        Long propertyId = 1L;

        when(favoriteRepository.existsByGuestIdAndPropertyId(guestId, propertyId)).thenReturn(true);

        // When
        boolean result = favoriteService.isFavorite(guestId, propertyId);

        // Then
        assertTrue(result);
    }

    @Test
    void isFavorite_ShouldReturnFalse_WhenFavoriteDoesNotExist() {
        // Given
        Long guestId = 1L;
        Long propertyId = 1L;

        when(favoriteRepository.existsByGuestIdAndPropertyId(guestId, propertyId)).thenReturn(false);

        // When
        boolean result = favoriteService.isFavorite(guestId, propertyId);

        // Then
        assertFalse(result);
    }

    @Test
    void getFavoriteCount_ShouldReturnCorrectCount() {
        // Given
        Long propertyId = 1L;
        long expectedCount = 5L;

        when(favoriteRepository.countByPropertyId(propertyId)).thenReturn(expectedCount);

        // When
        long result = favoriteService.getFavoriteCount(propertyId);

        // Then
        assertEquals(expectedCount, result);
    }
}
