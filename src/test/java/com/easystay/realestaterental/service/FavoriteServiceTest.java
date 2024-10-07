package com.easystay.realestaterental.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.easystay.realestaterental.dto.FavoriteDTO;
import com.easystay.realestaterental.entity.Favorite;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.exception.DuplicateResourceException;
import com.easystay.realestaterental.mapper.FavoriteMapper;
import com.easystay.realestaterental.repository.FavoriteRepository;
import com.easystay.realestaterental.repository.GuestRepository;
import com.easystay.realestaterental.repository.PropertyRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private GuestRepository guestRepository;
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private FavoriteMapper favoriteMapper;

    @InjectMocks
    private FavoriteService favoriteService;

    private Guest guest;
    private Property property;
    private Favorite favorite;
    private FavoriteDTO favoriteDTO;

    @BeforeEach
    void setUp() {
        guest = new Guest();
        guest.setId(1L);

        property = new Property();
        property.setId(1L);

        favorite = new Favorite();
        favorite.setId(1L);
        favorite.setGuest(guest);
        favorite.setProperty(property);

        favoriteDTO = new FavoriteDTO();
        favoriteDTO.setId(1L);
        favoriteDTO.setGuestId(1L);
        favoriteDTO.setPropertyId(1L);
    }

    @Test
    void testAddFavorite_Success() {
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(favoriteRepository.existsByGuestIdAndPropertyId(1L, 1L)).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);
        when(favoriteMapper.toDTO(favorite)).thenReturn(favoriteDTO);

        FavoriteDTO result = favoriteService.addFavorite(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void testAddFavorite_AlreadyExists() {
        when(favoriteRepository.existsByGuestIdAndPropertyId(1L, 1L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> favoriteService.addFavorite(1L, 1L));
    }

    @Test
    void testGetFavoritesByGuestId() {
        Pageable pageable = Pageable.unpaged();
        Page<Favorite> favoritePage = new PageImpl<>(List.of(favorite));
        when(guestRepository.existsById(1L)).thenReturn(true);
        when(favoriteRepository.findByGuestId(1L, pageable)).thenReturn(favoritePage);
        when(favoriteMapper.toDTO(favorite)).thenReturn(favoriteDTO);

        Page<FavoriteDTO> result = favoriteService.getFavoritesByGuestId(1L, pageable);

        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
    }

    @Test
    void testGetFavoritesByGuestId_GuestNotFound() {
        when(guestRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> favoriteService.getFavoritesByGuestId(1L, Pageable.unpaged()));
    }

    @Test
    void testRemoveFavorite_Success() {
        when(favoriteRepository.findByGuestIdAndPropertyId(1L, 1L)).thenReturn(Optional.of(favorite));

        favoriteService.removeFavorite(1L, 1L);

        verify(favoriteRepository).delete(favorite);
    }

    @Test
    void testRemoveFavorite_NotFound() {
        when(favoriteRepository.findByGuestIdAndPropertyId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> favoriteService.removeFavorite(1L, 1L));
    }

    @Test
    void testIsFavorite_True() {
        when(favoriteRepository.existsByGuestIdAndPropertyId(1L, 1L)).thenReturn(true);

        boolean result = favoriteService.isFavorite(1L, 1L);

        assertTrue(result);
    }

    @Test
    void testIsFavorite_False() {
        when(favoriteRepository.existsByGuestIdAndPropertyId(1L, 1L)).thenReturn(false);

        boolean result = favoriteService.isFavorite(1L, 1L);

        assertFalse(result);
    }

    @Test
    void testGetFavoriteCount() {
        when(propertyRepository.existsById(1L)).thenReturn(true);
        when(favoriteRepository.countByPropertyId(1L)).thenReturn(5L);

        long result = favoriteService.getFavoriteCount(1L);

        assertEquals(5L, result);
    }

    @Test
    void testGetFavoriteCount_PropertyNotFound() {
        when(propertyRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> favoriteService.getFavoriteCount(1L));
    }
}