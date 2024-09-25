package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Page<Favorite> findByGuestId(Long guestId, Pageable pageable);
    Optional<Favorite> findByGuestIdAndPropertyId(Long guestId, Long propertyId);
    boolean existsByGuestIdAndPropertyId(Long guestId, Long propertyId);
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.guest.id = :guestId AND f.property.id = :propertyId")
    void deleteByGuestIdAndPropertyId(@Param("guestId") Long guestId, @Param("propertyId") Long propertyId);
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.property.id = :propertyId")
    long countByPropertyId(@Param("propertyId") Long propertyId);
}