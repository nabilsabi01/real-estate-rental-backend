package com.easystay.realestaterental.repository;

import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.enums.PropertyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for managing Property entities.
 * Extends JpaSpecificationExecutor to provide specifications support for dynamic queries.
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {

    /**
     * Finds properties by the host's ID with pagination support.
     *
     * @param hostId  the ID of the host
     * @param pageable pagination information
     * @return a paginated list of properties associated with the specified host
     */
    Page<Property> findByHostId(Long hostId, Pageable pageable);

    /**
     * Finds properties by their type with pagination support.
     *
     * @param propertyType the type of the property
     * @param pageable     pagination information
     * @return a paginated list of properties of the specified type
     */
    Page<Property> findByPropertyType(PropertyType propertyType, Pageable pageable);

    /**
     * Finds properties with a price per night within a specified range.
     *
     * @param minPrice the minimum price per night
     * @param maxPrice the maximum price per night
     * @param pageable pagination information
     * @return a paginated list of properties within the specified price range
     */
    Page<Property> findByPricePerNightBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Finds properties located in a specific city with pagination support.
     *
     * @param city     the name of the city
     * @param pageable pagination information
     * @return a paginated list of properties in the specified city
     */
    @Query("SELECT p FROM Property p WHERE p.location.city = :city")
    Page<Property> findByCity(String city, Pageable pageable);

    /**
     * Finds properties located in a specific country with pagination support.
     *
     * @param country  the name of the country
     * @param pageable pagination information
     * @return a paginated list of properties in the specified country
     */
    @Query("SELECT p FROM Property p WHERE p.location.country = :country")
    Page<Property> findByCountry(String country, Pageable pageable);

    /**
     * Finds properties that have a specific amenity with pagination support.
     *
     * @param amenityId the ID of the amenity
     * @param pageable   pagination information
     * @return a paginated list of properties that have the specified amenity
     */
    @Query("SELECT p FROM Property p JOIN p.amenities a WHERE a.id = :amenityId")
    Page<Property> findByAmenityId(Long amenityId, Pageable pageable);

    /**
     * Finds properties that can accommodate a specified number of guests.
     *
     * @param guestCount the minimum number of guests the property can accommodate
     * @param pageable   pagination information
     * @return a paginated list of properties that meet the guest capacity
     */
    @Query("SELECT p FROM Property p WHERE p.maxGuests >= :guestCount")
    Page<Property> findByGuestCapacity(int guestCount, Pageable pageable);

    /**
     * Searches properties based on a keyword present in the title or description.
     *
     * @param keyword  the keyword to search for
     * @param pageable pagination information
     * @return a paginated list of properties matching the search criteria
     */
    @Query("SELECT p FROM Property p WHERE p.title LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Property> searchProperties(String keyword, Pageable pageable);

    /**
     * Finds the top 10 most recently created properties.
     *
     * @return a list of the top 10 properties ordered by creation date
     */
    List<Property> findTop10ByOrderByCreatedAtDesc();
}
