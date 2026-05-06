package com.example.bookmyshow.repositories;

import com.example.bookmyshow.entities.Booking;
import com.example.bookmyshow.entities.BookingStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"listing", "seat", "seat.screen"})
    List<Booking> findByListingIdOrderBySeatRowLabelAscSeatSeatNumberAsc(Long listingId);

    @EntityGraph(attributePaths = {"seat"})
    List<Booking> findByListingIdAndStatus(Long listingId, BookingStatus status);

    Optional<Booking> findByListingIdAndSeatId(Long listingId, Long seatId);
}
