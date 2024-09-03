package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostRepository extends JpaRepository<Host, Long> {
    List<Host> findBySuperHost(boolean isSuperHost);

    @Query("SELECT h FROM Host h WHERE h.firstName LIKE %:keyword% OR h.lastName LIKE %:keyword%")
    List<Host> searchHosts(String keyword);

    @Query("SELECT h FROM Host h JOIN h.properties p GROUP BY h HAVING COUNT(p) > :minProperties")
    List<Host> findHostsWithMultipleProperties(int minProperties);

    @Query("SELECT AVG(r.rating) FROM Host h JOIN h.properties p JOIN p.reviews r WHERE h.id = :hostId")
    Double getAverageRatingForHost(Long hostId);
}