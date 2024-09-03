package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.entity.Favorite;
import com.easystay.realestaterental.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Favorite>> getFavoritesByGuestId(@PathVariable Long guestId) {
        List<Favorite> favorites = favoriteService.getFavoritesByGuestId(guestId);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/guest/{guestId}/property/{propertyId}")
    public ResponseEntity<Favorite> addFavorite(@PathVariable Long guestId, @PathVariable Long propertyId) {
        Favorite favorite = favoriteService.addFavorite(guestId, propertyId);
        return ResponseEntity.ok(favorite);
    }

    @DeleteMapping("/guest/{guestId}/property/{propertyId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long guestId, @PathVariable Long propertyId) {
        favoriteService.removeFavorite(guestId, propertyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/guest/{guestId}/property/{propertyId}")
    public ResponseEntity<Boolean> isFavorite(@PathVariable Long guestId, @PathVariable Long propertyId) {
        boolean isFavorite = favoriteService.isFavorite(guestId, propertyId);
        return ResponseEntity.ok(isFavorite);
    }
}