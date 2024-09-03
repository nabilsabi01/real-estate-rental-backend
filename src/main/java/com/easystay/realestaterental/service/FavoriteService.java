package com.easystay.realestaterental.service;

import com.easystay.realestaterental.entity.Favorite;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.repository.FavoriteRepository;
import com.easystay.realestaterental.repository.GuestRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final GuestRepository guestRepository;

    private final PropertyRepository propertyRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, GuestRepository guestRepository, PropertyRepository propertyRepository) {
        this.favoriteRepository = favoriteRepository;
        this.guestRepository = guestRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<Favorite> getFavoritesByGuestId(Long guestId) {
        return favoriteRepository.findByGuestId(guestId);
    }

    @Transactional
    public Favorite addFavorite(Long guestId, Long propertyId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        if (favoriteRepository.existsByGuestIdAndPropertyId(guestId, propertyId)) {
            throw new IllegalStateException("Property is already in favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setGuest(guest);
        favorite.setProperty(property);
        return favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(Long guestId, Long propertyId) {
        if (!favoriteRepository.existsByGuestIdAndPropertyId(guestId, propertyId)) {
            throw new ResourceNotFoundException("Favorite not found");
        }
        favoriteRepository.deleteByGuestIdAndPropertyId(guestId, propertyId);
    }

    public boolean isFavorite(Long guestId, Long propertyId) {
        return favoriteRepository.existsByGuestIdAndPropertyId(guestId, propertyId);
    }
}