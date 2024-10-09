package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Page<Favorite> findByGuestId(Long guestId, Pageable pageable);
    Optional<Favorite> findByGuestIdAndPropertyId(Long guestId, Long propertyId);
    boolean existsByGuestIdAndPropertyId(Long guestId, Long propertyId);
    void deleteByGuestIdAndPropertyId(Long guestId, Long propertyId);
    long countByPropertyId(Long propertyId);
}