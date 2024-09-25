package com.easystay.realestaterental.service;

import com.easystay.realestaterental.dto.BookingDTO;
import com.easystay.realestaterental.entity.Booking;
import com.easystay.realestaterental.entity.Guest;
import com.easystay.realestaterental.entity.Property;
import com.easystay.realestaterental.enums.BookingStatus;
import com.easystay.realestaterental.exception.BookingConflictException;
import com.easystay.realestaterental.exception.ResourceNotFoundException;
import com.easystay.realestaterental.mapper.BookingMapper;
import com.easystay.realestaterental.repository.BookingRepository;
import com.easystay.realestaterental.repository.GuestRepository;
import com.easystay.realestaterental.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final GuestRepository guestRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Property property = propertyRepository.findById(bookingDTO.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        Guest guest = guestRepository.findById(bookingDTO.getGuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                property.getId(),
                bookingDTO.getCheckInDate(),
                bookingDTO.getCheckOutDate(),
                Arrays.asList(BookingStatus.CANCELED, BookingStatus.COMPLETED)
        );

        if (!overlappingBookings.isEmpty()) {
            throw new BookingConflictException("The property is not available for the selected dates");
        }

        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking.setProperty(property);
        booking.setGuest(guest);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDTO(savedBooking);
    }

    public BookingDTO getBooking(Long id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public Page<BookingDTO> getBookingsByGuest(Long guestId, Pageable pageable) {
        return bookingRepository.findByGuestId(guestId, pageable)
                .map(bookingMapper::toDTO);
    }

    public Page<BookingDTO> getBookingsByProperty(Long propertyId, Pageable pageable) {
        return bookingRepository.findByPropertyId(propertyId, pageable)
                .map(bookingMapper::toDTO);
    }

    @Transactional
    public BookingDTO updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(status);
        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toDTO(updatedBooking);
    }

    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }
}