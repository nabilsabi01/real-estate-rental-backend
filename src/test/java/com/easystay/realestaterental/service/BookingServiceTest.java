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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

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

    private BookingDTO bookingDTO;
    private Booking booking;
    private Property property;
    private Guest guest;

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
        booking.setCheckInDate(LocalDate.now());
        booking.setCheckOutDate(LocalDate.now().plusDays(2));
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(BigDecimal.valueOf(200));

        bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setGuestId(1L);
        bookingDTO.setPropertyId(1L);
        bookingDTO.setCheckInDate(LocalDate.now());
        bookingDTO.setCheckOutDate(LocalDate.now().plusDays(2));
        bookingDTO.setStatus(BookingStatus.PENDING);
        bookingDTO.setTotalPrice(BigDecimal.valueOf(200));
    }

    @Test
    void createBooking_ShouldReturnBookingDTO() {
        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(guestRepository.findById(anyLong())).thenReturn(Optional.of(guest));
        when(bookingRepository.findOverlappingBookings(anyLong(), any(), any(), anyList())).thenReturn(Collections.emptyList());
        when(bookingMapper.toEntity(any(BookingDTO.class))).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDTO(any(Booking.class))).thenReturn(bookingDTO);

        BookingDTO result = bookingService.createBooking(bookingDTO);

        assertNotNull(result);
        assertEquals(bookingDTO, result);
    }

    @Test
    void createBooking_OverlappingBookings_ShouldThrowBookingConflictException() {
        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(guestRepository.findById(anyLong())).thenReturn(Optional.of(guest));
        when(bookingRepository.findOverlappingBookings(anyLong(), any(), any(), anyList())).thenReturn(Collections.singletonList(booking));

        assertThrows(BookingConflictException.class, () -> bookingService.createBooking(bookingDTO));
    }

    @Test
    void getBooking_ShouldReturnBookingDTO() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toDTO(any(Booking.class))).thenReturn(bookingDTO);

        BookingDTO result = bookingService.getBooking(1L);

        assertNotNull(result);
        assertEquals(bookingDTO, result);
    }

    @Test
    void getBookingsByGuest_ShouldReturnPageOfBookingDTOs() {
        Page<Booking> bookingPage = new PageImpl<>(Collections.singletonList(booking));
        when(bookingRepository.findByGuestId(anyLong(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingMapper.toDTO(any(Booking.class))).thenReturn(bookingDTO);

        Page<BookingDTO> result = bookingService.getBookingsByGuest(1L, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(bookingDTO, result.getContent().get(0));
    }

    @Test
    void updateBookingStatus_ShouldReturnUpdatedBookingDTO() {
        // Arrange
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        // Simuler la mise à jour du statut
        Booking updatedBooking = new Booking();
        updatedBooking.setStatus(BookingStatus.CONFIRMED);
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);

        // Simuler le mapper pour renvoyer un DTO avec le statut mis à jour
        BookingDTO updatedBookingDTO = new BookingDTO();
        updatedBookingDTO.setStatus(BookingStatus.CONFIRMED);
        when(bookingMapper.toDTO(any(Booking.class))).thenReturn(updatedBookingDTO);

        // Act
        BookingDTO result = bookingService.updateBookingStatus(1L, BookingStatus.CONFIRMED);

        // Assert
        assertNotNull(result);
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());

        // Vérifier que la méthode save a été appelée avec le bon statut
        verify(bookingRepository).save(argThat(booking ->
                booking.getStatus() == BookingStatus.CONFIRMED
        ));
    }

    @Test
    void cancelBooking_ShouldUpdateBookingStatus() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        assertDoesNotThrow(() -> bookingService.cancelBooking(1L));
        assertEquals(BookingStatus.CANCELED, booking.getStatus());
    }

    @Test
    void isBookingOwner_ShouldReturnTrue() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        boolean result = bookingService.isBookingOwner(1L, 1L);

        assertTrue(result);
    }
}