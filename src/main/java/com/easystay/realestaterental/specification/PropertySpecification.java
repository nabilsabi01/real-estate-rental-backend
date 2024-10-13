package com.easystay.realestaterental.specification;

import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.entity.Booking;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class PropertySpecification {

    public static Specification<Property> withDestination(String destination) {
        return (root, query, cb) -> {
            if (destination == null || destination.isEmpty()) {
                return cb.conjunction();
            }
            String lowercaseDestination = "%" + destination.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("location").get("city")), lowercaseDestination),
                    cb.like(cb.lower(root.get("location").get("country")), lowercaseDestination)
            );
        };
    }

    public static Specification<Property> withAvailableDates(LocalDate checkInDate, LocalDate checkOutDate) {
        return (root, query, cb) -> {
            if (checkInDate == null || checkOutDate == null) {
                return cb.conjunction();
            }
            Join<Property, Booking> bookingJoin = root.join("bookings", JoinType.LEFT);
            return cb.or(
                    cb.isNull(bookingJoin.get("id")),
                    cb.lessThan(bookingJoin.get("checkOutDate"), checkInDate),
                    cb.greaterThan(bookingJoin.get("checkInDate"), checkOutDate)
            );
        };
    }

    public static Specification<Property> withMaxGuests(Integer guests) {
        return (root, query, cb) -> {
            if (guests == null || guests <= 0) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("maxGuests"), guests);
        };
    }
}