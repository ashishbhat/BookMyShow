package com.example.bookmyshow.services;

import com.example.bookmyshow.dto.ListingSearchResponse;
import com.example.bookmyshow.entities.Booking;
import com.example.bookmyshow.entities.BookingStatus;
import com.example.bookmyshow.entities.Listing;
import com.example.bookmyshow.entities.Movie;
import com.example.bookmyshow.entities.Screen;
import com.example.bookmyshow.entities.Seat;
import com.example.bookmyshow.entities.Venue;
import com.example.bookmyshow.repositories.BookingRepository;
import com.example.bookmyshow.repositories.ListingRepository;
import com.example.bookmyshow.repositories.MovieRepository;
import com.example.bookmyshow.repositories.ScreenRepository;
import com.example.bookmyshow.repositories.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public Listing addListing(Listing listing) {
        listing.setMovie(resolveMovie(listing.getMovie()));
        listing.setScreen(resolveScreen(listing.getScreen()));
        Listing savedListing = listingRepository.save(listing);
        createAvailableBookings(savedListing);
        return savedListing;
    }

    @Transactional
    public void deleteListing(Long id) {
        if (!listingRepository.existsById(id)) {
            throw new EntityNotFoundException("Listing not found with id: " + id);
        }
        listingRepository.deleteById(id);
    }

    @Transactional
    public Listing editListing(Long id, Listing updates) {
        Listing existing = listingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found with id: " + id));

        if (updates.getName() != null) {
            existing.setName(updates.getName());
        }
        if (updates.getPrice() != null) {
            existing.setPrice(updates.getPrice());
        }
        if (updates.getMovie() != null) {
            existing.setMovie(resolveMovie(updates.getMovie()));
        }
        if (updates.getStart() != null) {
            existing.setStart(updates.getStart());
        }
        if (updates.getEnd() != null) {
            existing.setEnd(updates.getEnd());
        }
        if (updates.getDate() != null) {
            existing.setDate(updates.getDate());
        }
        if (updates.getScreen() != null) {
            existing.setScreen(resolveScreen(updates.getScreen()));
        }
        return existing;
    }

    @Transactional(readOnly = true)
    public Listing getListingById(Long id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Listing not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ListingSearchResponse> getListingsByMovie(Long movieId, LocalDate date) {
        if (!movieRepository.existsById(movieId)) {
            throw new EntityNotFoundException("Movie not found with id: " + movieId);
        }

        List<Listing> listings = date == null
                ? listingRepository.findByMovieIdOrderByDateAscStartAsc(movieId)
                : listingRepository.findByMovieIdAndDateOrderByStartAsc(movieId, date);

        return listings.stream()
                .map(this::toListingSearchResponse)
                .toList();
    }

    private ListingSearchResponse toListingSearchResponse(Listing listing) {
        Movie movie = listing.getMovie();
        Screen screen = listing.getScreen();
        Venue venue = screen.getVenue();

        return new ListingSearchResponse(
                listing.getId(),
                listing.getName(),
                listing.getPrice(),
                movie.getId(),
                movie.getName(),
                movie.getRating(),
                movie.getUaRating(),
                listing.getDate(),
                listing.getStart(),
                listing.getEnd(),
                screen.getId(),
                screen.getType(),
                venue.getId(),
                venue.getAddress(),
                venue.getType()
        );
    }

    private void createAvailableBookings(Listing listing) {
        List<Seat> seats = seatRepository.findByScreenIdOrderByRowLabelAscSeatNumberAsc(listing.getScreen().getId());
        List<Booking> bookings = seats.stream()
                .map(seat -> Booking.builder()
                        .listing(listing)
                        .seat(seat)
                        .status(BookingStatus.AVAILABLE)
                        .price(listing.getPrice())
                        .build())
                .toList();
        bookingRepository.saveAll(bookings);
    }

    private Movie resolveMovie(Movie movie) {
        if (movie == null || movie.getId() == null) {
            throw new EntityNotFoundException("Movie id is required");
        }
        return movieRepository.findById(movie.getId())
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movie.getId()));
    }

    private Screen resolveScreen(Screen screen) {
        if (screen == null || screen.getId() == null) {
            throw new EntityNotFoundException("Screen id is required");
        }
        return screenRepository.findById(screen.getId())
                .orElseThrow(() -> new EntityNotFoundException("Screen not found with id: " + screen.getId()));
    }
}
