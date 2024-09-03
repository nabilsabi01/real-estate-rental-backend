package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByGuestId(Long guestId);

    Optional<Favorite> findByGuestIdAndPropertyId(Long guestId, Long propertyId);

    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.property.id = :propertyId")
    long countByPropertyId(Long propertyId);

    @Query("SELECT f.property.id FROM Favorite f GROUP BY f.property.id ORDER BY COUNT(f) DESC")
    List<Long> findMostFavoritedPropertyIds(Pageable pageable);

    boolean existsByGuestIdAndPropertyId(Long guestId, Long propertyId);
}