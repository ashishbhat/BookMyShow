package com.example.bookmyshow.dto;

import com.example.bookmyshow.entities.BookingStatus;
import com.example.bookmyshow.entities.SeatType;
import com.example.bookmyshow.entities.VenueType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record BookingResponse(
        Long id,
        Long listingId,
        String listingName,
        Long movieId,
        String movieName,
        Double movieRating,
        LocalDate date,
        LocalTime start,
        LocalTime end,
        Long venueId,
        String venueAddress,
        VenueType venueType,
        Long screenId,
        String screenType,
        Long seatId,
        String seat,
        String rowLabel,
        Integer seatNumber,
        SeatType seatType,
        BookingStatus status,
        BigDecimal price,
        String customerName
) {
}
