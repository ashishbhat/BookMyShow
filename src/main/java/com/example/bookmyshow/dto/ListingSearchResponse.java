package com.example.bookmyshow.dto;

import com.example.bookmyshow.entities.CertificationRating;
import com.example.bookmyshow.entities.VenueType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record ListingSearchResponse(
        Long listingId,
        String listingName,
        BigDecimal price,
        Long movieId,
        String movieName,
        Double rating,
        CertificationRating uaRating,
        LocalDate date,
        LocalTime start,
        LocalTime end,
        Long screenId,
        String screenType,
        Long venueId,
        String venueAddress,
        VenueType venueType
) {
}
