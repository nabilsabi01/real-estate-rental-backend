package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.FavoriteDTO;
import com.easystay.realestaterental.entity.Favorite;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.mapper.FavoriteMapper;
import com.easystay.realestaterental.repository.FavoriteRepository;
import com.easystay.realestaterental.repository.GuestRepository;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    void addFavorite_ShouldReturnFavoriteDTO() {
        when(guestRepository.findById(anyLong())).thenReturn(Optional.of(guest));
        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(favoriteRepository.existsByGuestIdAndPropertyId(anyLong(), anyLong())).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);
        when(favoriteMapper.toDTO(any(Favorite.class))).thenReturn(favoriteDTO);

        FavoriteDTO result = favoriteService.addFavorite(1L, 1L);

        assertNotNull(result);
        assertEquals(favoriteDTO, result);
    }

    @Test
    void addFavorite_ExistingFavorite_ShouldThrowIllegalStateException() {
        when(guestRepository.findById(anyLong())).thenReturn(Optional.of(guest));
        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(favoriteRepository.existsByGuestIdAndPropertyId(anyLong(), anyLong())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> favoriteService.addFavorite(1L, 1L));
    }

    @Test
    void getFavoritesByGuestId_ShouldReturnPageOfFavoriteDTOs() {
        Page<Favorite> favoritePage = new PageImpl<>(Collections.singletonList(favorite));
        when(favoriteRepository.findByGuestId(anyLong(), any(Pageable.class))).thenReturn(favoritePage);
        when(favoriteMapper.toDTO(any(Favorite.class))).thenReturn(favoriteDTO);

        Page<FavoriteDTO> result = favoriteService.getFavoritesByGuestId(1L, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(favoriteDTO, result.getContent().get(0));
    }

    @Test
    void removeFavorite_ShouldCallRepositoryDeleteMethod() {
        favoriteService.removeFavorite(1L, 1L);

        verify(favoriteRepository).deleteByGuestIdAndPropertyId(1L, 1L);
    }

    @Test
    void isFavorite_ShouldReturnTrue() {
        when(favoriteRepository.existsByGuestIdAndPropertyId(anyLong(), anyLong())).thenReturn(true);

        boolean result = favoriteService.isFavorite(1L, 1L);

        assertTrue(result);
    }

    @Test
    void getFavoriteCount_ShouldReturnCorrectCount() {
        when(favoriteRepository.countByPropertyId(anyLong())).thenReturn(5L);

        long result = favoriteService.getFavoriteCount(1L);

        assertEquals(5L, result);
    }
}