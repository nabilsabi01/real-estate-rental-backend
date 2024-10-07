package com.easystay.realestaterental.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private GuestRepository guestRepository;
    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;
    private BookingDTO bookingDTO;
    private Guest guest;
    private Property property;

    @BeforeEach
    void setUp() {
        guest = new Guest();
        guest.setId(1L);

        property = new Property();
        property.setId(1L);

        booking = new Booking();
        booking.setId(1L);
        booking.setGuest(guest);
        booking.setProperty(property);
        booking.setCheckInDate(LocalDate.now().plusDays(1));
        booking.setCheckOutDate(LocalDate.now().plusDays(3));
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(BigDecimal.valueOf(200));

        bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setGuestId(1L);
        bookingDTO.setPropertyId(1L);
        bookingDTO.setCheckInDate(LocalDate.now().plusDays(1));
        bookingDTO.setCheckOutDate(LocalDate.now().plusDays(3));
        bookingDTO.setStatus(BookingStatus.PENDING);
        bookingDTO.setTotalPrice(BigDecimal.valueOf(200));
    }

    @Test
    void testUpdateBookingStatus_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking savedBooking = invocation.getArgument(0);
            savedBooking.setStatus(BookingStatus.CONFIRMED);
            return savedBooking;
        });
        when(bookingMapper.toDTO(any(Booking.class))).thenAnswer(invocation -> {
            Booking savedBooking = invocation.getArgument(0);
            bookingDTO.setStatus(savedBooking.getStatus());
            return bookingDTO;
        });

        BookingDTO result = bookingService.updateBookingStatus(1L, BookingStatus.CONFIRMED);

        assertNotNull(result);
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void testCreateBooking_Success() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(bookingRepository.findOverlappingBookings(anyLong(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(bookingMapper.toEntity(any(BookingDTO.class))).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDTO(any(Booking.class))).thenReturn(bookingDTO);

        BookingDTO result = bookingService.createBooking(bookingDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(BookingStatus.PENDING, result.getStatus());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testCreateBooking_Conflict() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(bookingRepository.findOverlappingBookings(anyLong(), any(), any(), any()))
                .thenReturn(List.of(booking));

        assertThrows(BookingConflictException.class, () -> bookingService.createBooking(bookingDTO));
    }

    @Test
    void testGetBooking_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDTO(booking)).thenReturn(bookingDTO);

        BookingDTO result = bookingService.getBooking(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetBooking_NotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.getBooking(1L));
    }

    @Test
    void testGetBookingsByGuest() {
        Page<Booking> bookingPage = new PageImpl<>(List.of(booking));
        when(bookingRepository.findByGuestId(1L, Pageable.unpaged())).thenReturn(bookingPage);
        when(bookingMapper.toDTO(booking)).thenReturn(bookingDTO);

        Page<BookingDTO> result = bookingService.getBookingsByGuest(1L, Pageable.unpaged());

        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
    }

    @Test
    void testGetBookingsByProperty() {
        Page<Booking> bookingPage = new PageImpl<>(List.of(booking));
        when(bookingRepository.findByPropertyId(1L, Pageable.unpaged())).thenReturn(bookingPage);
        when(bookingMapper.toDTO(booking)).thenReturn(bookingDTO);

        Page<BookingDTO> result = bookingService.getBookingsByProperty(1L, Pageable.unpaged());

        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
    }

    @Test
    void testUpdateBookingStatus_NotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.updateBookingStatus(1L, BookingStatus.CONFIRMED));
    }

    @Test
    void testCancelBooking_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        bookingService.cancelBooking(1L);

        assertEquals(BookingStatus.CANCELED, booking.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void testCancelBooking_NotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.cancelBooking(1L));
    }
}