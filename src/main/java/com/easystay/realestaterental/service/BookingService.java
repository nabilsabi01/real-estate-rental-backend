package com.easystay.realestaterental.service;

import com.easystay.realestaterental.entity.Booking;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.enums.BookingStatus;
import com.easystay.realestaterental.exception.BookingConflictException;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.repository.BookingRepository;
import com.easystay.realestaterental.repository.GuestRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final PropertyRepository propertyRepository;

    private final GuestRepository guestRepository;

    public BookingService(BookingRepository bookingRepository, PropertyRepository propertyRepository, GuestRepository guestRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.guestRepository = guestRepository;
    }

    public List<Booking> getBookingsByGuestId(Long guestId) {
        return bookingRepository.findByGuestId(guestId);
    }

    public List<Booking> getBookingsByPropertyId(Long propertyId) {
        return bookingRepository.findByPropertyId(propertyId);
    }

    public List<Booking> getBookingsByHostId(Long hostId) {
        return bookingRepository.findByHostId(hostId);
    }

    @Transactional
    public Booking createBooking(Long propertyId, Long guestId, LocalDate checkInDate, LocalDate checkOutDate, Integer numberOfGuests) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        if (!isPropertyAvailable(propertyId, checkInDate, checkOutDate)) {
            throw new BookingConflictException("Property is not available for the selected dates");
        }

        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BigDecimal totalPrice = property.getPricePerNight().multiply(BigDecimal.valueOf(numberOfNights));

        Booking booking = new Booking();
        booking.setProperty(property);
        booking.setGuest(guest);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(totalPrice);
        booking.setNumberOfGuests(numberOfGuests);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBookingStatus(Long bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(newStatus);
        return bookingRepository.save(booking);
    }

    public boolean isPropertyAvailable(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(propertyId, checkInDate, checkOutDate);
        return conflictingBookings.isEmpty();
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }
}