package com.easystay.realestaterental.controller;

import com.easystay.realestaterental.dto.BookingDTO;
import com.easystay.realestaterental.enums.BookingStatus;
import com.easystay.realestaterental.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Booking management APIs")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('GUEST')")
    @Operation(summary = "Create a new booking", responses = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Booking dates conflict with existing bookings")
    })
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GUEST', 'HOST', 'ADMIN')")
    @Operation(summary = "Get a booking by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Booking retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @GetMapping("/guests/{guestId}")
    @PreAuthorize("hasRole('GUEST') and #guestId == authentication.principal.id")
    @Operation(summary = "Get bookings for a guest", responses = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully")
    })
    public ResponseEntity<Page<BookingDTO>> getBookingsByGuest(
            @PathVariable Long guestId,
            @PageableDefault(sort = "checkInDate") Pageable pageable) {
        return ResponseEntity.ok(bookingService.getBookingsByGuest(guestId, pageable));
    }

    @GetMapping("/properties/{propertyId}")
    @PreAuthorize("hasRole('HOST')")
    @Operation(summary = "Get bookings for a property", responses = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully")
    })
    public ResponseEntity<Page<BookingDTO>> getBookingsByProperty(
            @PathVariable Long propertyId,
            @PageableDefault(sort = "checkInDate") Pageable pageable) {
        return ResponseEntity.ok(bookingService.getBookingsByProperty(propertyId, pageable));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @Operation(summary = "Update booking status", responses = {
            @ApiResponse(responseCode = "200", description = "Booking status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable Long id,
            @Parameter(description = "New booking status", required = true)
            @RequestParam BookingStatus status) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GUEST') and @bookingService.isBookingOwner(#id, authentication.principal.id)")
    @Operation(summary = "Cancel a booking", responses = {
            @ApiResponse(responseCode = "204", description = "Booking canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}