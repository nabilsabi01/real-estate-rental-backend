package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.FavoriteDTO;
import com.easystay.realestaterental.entity.Favorite;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.FavoriteMapper;
import com.easystay.realestaterental.repository.FavoriteRepository;
import com.easystay.realestaterental.repository.GuestRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final GuestRepository guestRepository;
    private final PropertyRepository propertyRepository;
    private final FavoriteMapper favoriteMapper;

    @Transactional
    public FavoriteDTO addFavorite(Long guestId, Long propertyId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        if (favoriteRepository.existsByGuestIdAndPropertyId(guestId, propertyId)) {
            throw new IllegalStateException("Property is already favorited by this guest");
        }

        Favorite favorite = new Favorite();
        favorite.setGuest(guest);
        favorite.setProperty(property);

        Favorite savedFavorite = favoriteRepository.save(favorite);
        return favoriteMapper.toDTO(savedFavorite);
    }

    public Page<FavoriteDTO> getFavoritesByGuestId(Long guestId, Pageable pageable) {
        return favoriteRepository.findByGuestId(guestId, pageable)
                .map(favoriteMapper::toDTO);
    }

    @Transactional
    public void removeFavorite(Long guestId, Long propertyId) {
        favoriteRepository.deleteByGuestIdAndPropertyId(guestId, propertyId);
    }

    public boolean isFavorite(Long guestId, Long propertyId) {
        return favoriteRepository.existsByGuestIdAndPropertyId(guestId, propertyId);
    }

    public long getFavoriteCount(Long propertyId) {
        return favoriteRepository.countByPropertyId(propertyId);
    }
}