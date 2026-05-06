package com.example.bookmyshow.dto;

import com.example.bookmyshow.entities.SeatType;

import java.math.BigDecimal;

public record SeatAvailabilityResponse(
        Long seatId,
        String rowLabel,
        Integer seatNumber,
        SeatType seatType,
        String status,
        BigDecimal price,
        Long bookingId,
        String customerName
) {
}
