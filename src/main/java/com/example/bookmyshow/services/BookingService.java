package com.example.bookmyshow.services;

import com.example.bookmyshow.dto.BookingRequest;
import com.example.bookmyshow.dto.BookingResponse;
import com.example.bookmyshow.dto.SeatAvailabilityResponse;
import com.example.bookmyshow.entities.Booking;
import com.example.bookmyshow.entities.BookingStatus;
import com.example.bookmyshow.entities.Listing;
import com.example.bookmyshow.entities.Seat;
import com.example.bookmyshow.entities.Screen;
import com.example.bookmyshow.entities.Venue;
import com.example.bookmyshow.repositories.BookingRepository;
import com.example.bookmyshow.repositories.ListingRepository;
import com.example.bookmyshow.repositories.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ListingRepository listingRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public BookingResponse bookSeat(Long id, BookingRequest request) {
        if (id == null) {
            throw new EntityNotFoundException("Booking id is required");
        }
        if (request == null || request.customerName() == null || request.customerName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }

        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
        if (existing.getStatus() == BookingStatus.BOOKED) {
            throw new IllegalStateException("Seat is already booked");
        }

        existing.setCustomerName(request.customerName().trim());
        existing.setStatus(BookingStatus.BOOKED);
        return toBookingResponse(existing);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
        return toBookingResponse(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::toBookingResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByListingId(Long listingId) {
        if (!listingRepository.existsById(listingId)) {
            throw new EntityNotFoundException("Listing not found with id: " + listingId);
        }
        return bookingRepository.findByListingIdOrderBySeatRowLabelAscSeatSeatNumberAsc(listingId).stream()
                .map(this::toBookingResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SeatAvailabilityResponse> getSeatAvailabilityByListingId(Long listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found with id: " + listingId));

        Map<Long, Booking> bookingBySeatId = bookingRepository
                .findByListingIdOrderBySeatRowLabelAscSeatSeatNumberAsc(listingId)
                .stream()
                .collect(Collectors.toMap(booking -> booking.getSeat().getId(), booking -> booking));

        return seatRepository.findByScreenIdOrderByRowLabelAscSeatNumberAsc(listing.getScreen().getId())
                .stream()
                .map(seat -> {
                    Booking booking = bookingBySeatId.get(seat.getId());
                    return new SeatAvailabilityResponse(
                            seat.getId(),
                            seat.getRowLabel(),
                            seat.getSeatNumber(),
                            seat.getSeatType(),
                            booking == null ? BookingStatus.AVAILABLE.name() : booking.getStatus().name(),
                            booking == null ? listing.getPrice() : booking.getPrice(),
                            booking == null ? null : booking.getId(),
                            booking == null ? null : booking.getCustomerName()
                    );
                })
                .toList();
    }

    private BookingResponse toBookingResponse(Booking booking) {
        Listing listing = booking.getListing();
        Seat seat = booking.getSeat();
        Screen screen = listing.getScreen();
        Venue venue = screen.getVenue();

        return new BookingResponse(
                booking.getId(),
                listing.getId(),
                listing.getName(),
                listing.getMovie().getId(),
                listing.getMovie().getName(),
                listing.getMovie().getRating(),
                listing.getDate(),
                listing.getStart(),
                listing.getEnd(),
                venue.getId(),
                venue.getAddress(),
                venue.getType(),
                screen.getId(),
                screen.getType(),
                seat.getId(),
                seat.getRowLabel() + seat.getSeatNumber(),
                seat.getRowLabel(),
                seat.getSeatNumber(),
                seat.getSeatType(),
                booking.getStatus(),
                booking.getPrice(),
                booking.getCustomerName()
        );
    }
}
