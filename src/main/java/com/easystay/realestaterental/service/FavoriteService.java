package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.FavoriteDTO;
import com.easystay.realestaterental.entity.Favorite;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.exception.DuplicateResourceException;
import com.easystay.realestaterental.exception.BadRequestException;
import com.easystay.realestaterental.mapper.FavoriteMapper;
import com.easystay.realestaterental.repository.FavoriteRepository;
import com.easystay.realestaterental.repository.GuestRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        validateIds(guestId, propertyId);

        return Optional.of(new Favorite())
                .filter(f -> !favoriteRepository.existsByGuestIdAndPropertyId(guestId, propertyId))
                .map(favorite -> {
                    Guest guest = findGuest(guestId);
                    Property property = findProperty(propertyId);
                    favorite.setGuest(guest);
                    favorite.setProperty(property);
                    return favoriteRepository.save(favorite);
                })
                .map(favoriteMapper::toDTO)
                .orElseThrow(() -> new DuplicateResourceException("Property is already in favorites"));
    }

    public Page<FavoriteDTO> getFavoritesByGuestId(Long guestId, Pageable pageable) {
        validateId(guestId, "Guest ID");
        ensureGuestExists(guestId);

        return favoriteRepository.findByGuestId(guestId, pageable)
                .map(favoriteMapper::toDTO);
    }

    @Transactional
    public void removeFavorite(Long guestId, Long propertyId) {
        validateIds(guestId, propertyId);

        favoriteRepository.findByGuestIdAndPropertyId(guestId, propertyId)
                .ifPresentOrElse(
                        favoriteRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException("Favorite not found for guest id: " + guestId + " and property id: " + propertyId);
                        }
                );
    }

    public boolean isFavorite(Long guestId, Long propertyId) {
        validateIds(guestId, propertyId);
        return favoriteRepository.existsByGuestIdAndPropertyId(guestId, propertyId);
    }

    public long getFavoriteCount(Long propertyId) {
        validateId(propertyId, "Property ID");
        ensurePropertyExists(propertyId);
        return favoriteRepository.countByPropertyId(propertyId);
    }

    private void validateIds(Long guestId, Long propertyId) {
        validateId(guestId, "Guest ID");
        validateId(propertyId, "Property ID");
    }

    private void validateId(Long id, String fieldName) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new BadRequestException(fieldName + " must not be null"));
    }

    private Guest findGuest(Long guestId) {
        return guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id: " + guestId));
    }

    private Property findProperty(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));
    }

    private void ensureGuestExists(Long guestId) {
        if (!guestRepository.existsById(guestId)) {
            throw new ResourceNotFoundException("Guest not found with id: " + guestId);
        }
    }

    private void ensurePropertyExists(Long propertyId) {
        if (!propertyRepository.existsById(propertyId)) {
            throw new ResourceNotFoundException("Property not found with id: " + propertyId);
        }
    }
}