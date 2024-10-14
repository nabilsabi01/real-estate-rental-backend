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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing favorites.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final GuestRepository guestRepository;
    private final PropertyRepository propertyRepository;
    private final FavoriteMapper favoriteMapper;
    private final PropertyMapper propertyMapper;

    /**
     * Adds a property to a guest's favorites.
     *
     * @param guestId    the ID of the guest
     * @param propertyId the ID of the property to favorite
     * @return the created favorite DTO
     * @throws ResourceNotFoundException if the guest or property is not found
     * @throws IllegalStateException if the property is already favorited by the guest
     */
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
        FavoriteDTO favoriteDTO = favoriteMapper.toDTO(savedFavorite);

        PropertyDTO propertyDTO = propertyMapper.toDTO(property);
        propertyDTO.setIsFavorited(true);
        favoriteDTO.setProperty(propertyDTO);

        return favoriteDTO;
    }

    /**
     * Removes a property from a guest's favorites.
     *
     * @param guestId    the ID of the guest
     * @param propertyId the ID of the property to unfavorite
     */
    @Transactional
    public void removeFavorite(Long guestId, Long propertyId) {
        favoriteRepository.deleteByGuestIdAndPropertyId(guestId, propertyId);
    }

    /**
     * Gets all favorites for a given guest.
     *
     * @param guestId  the ID of the guest
     * @param pageable the pagination information
     * @return a page of favorite DTOs
     */
    public Page<FavoriteDTO> getFavoritesByGuestId(Long guestId, Pageable pageable) {
        return favoriteRepository.findByGuestId(guestId, pageable)
                .map(favorite -> {
                    FavoriteDTO dto = favoriteMapper.toDTO(favorite);
                    if (dto.getProperty() != null) {
                        dto.getProperty().setIsFavorited(true);
                    }
                    return dto;
                });
    }


    /**
     * Gets the number of times a property has been favorited.
     *
     * @param propertyId the ID of the property
     * @return the count of favorites for the property
     */
    public long getFavoriteCount(Long propertyId) {
        return favoriteRepository.countByPropertyId(propertyId);
    }

    /**
     * Checks if a property is favorited by a guest.
     *
     * @param guestId    the ID of the guest
     * @param propertyId the ID of the property
     * @return true if the property is favorited by the guest, false otherwise
     */
    public boolean isFavorite(Long guestId, Long propertyId) {
        return favoriteRepository.existsByGuestIdAndPropertyId(guestId, propertyId);
    }
}