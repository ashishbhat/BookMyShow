package com.example.bookmyshow.controllers;

import com.example.bookmyshow.dto.BookingResponse;
import com.example.bookmyshow.dto.SeatAvailabilityResponse;
import com.example.bookmyshow.entities.Listing;
import com.example.bookmyshow.services.BookingService;
import com.example.bookmyshow.services.ListingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Listing addListing(@RequestBody Listing listing) {
        return listingService.addListing(listing);
    }

    @GetMapping
    public List<Listing> getAllListings() {
        return listingService.getAllListings();
    }

    @GetMapping("/{id}")
    public Listing getListingById(@PathVariable Long id) {
        return listingService.getListingById(id);
    }

    @GetMapping("/{listingId}/seats")
    public List<SeatAvailabilityResponse> getSeatAvailabilityByListingId(@PathVariable Long listingId) {
        return bookingService.getSeatAvailabilityByListingId(listingId);
    }

    @GetMapping("/{listingId}/bookings")
    public List<BookingResponse> getBookingsByListingId(@PathVariable Long listingId) {
        return bookingService.getBookingsByListingId(listingId);
    }

    @PatchMapping("/{id}")
    public Listing editListing(@PathVariable Long id, @RequestBody Listing updates) {
        return listingService.editListing(id, updates);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
