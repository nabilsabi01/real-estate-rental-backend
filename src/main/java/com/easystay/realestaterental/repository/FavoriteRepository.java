package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByGuestId(Long guestId);
    Optional<Favorite> findByGuestIdAndPropertyId(Long guestId, Long propertyId);
    boolean existsByGuestIdAndPropertyId(Long guestId, Long propertyId);
    void deleteByGuestIdAndPropertyId(Long guestId, Long propertyId);
}