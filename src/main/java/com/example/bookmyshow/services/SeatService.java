package com.example.bookmyshow.services;

import com.example.bookmyshow.entities.Screen;
import com.example.bookmyshow.entities.Seat;
import com.example.bookmyshow.repositories.ScreenRepository;
import com.example.bookmyshow.repositories.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;

    @Transactional
    public Seat addSeat(Seat seat) {
        seat.setScreen(resolveScreen(seat.getScreen()));
        return seatRepository.save(seat);
    }

    @Transactional
    public void deleteSeat(Long id) {
        if (!seatRepository.existsById(id)) {
            throw new EntityNotFoundException("Seat not found with id: " + id);
        }
        seatRepository.deleteById(id);
    }

    @Transactional
    public Seat editSeat(Long id, Seat updates) {
        Seat existing = seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found with id: " + id));

        if (updates.getScreen() != null) {
            existing.setScreen(resolveScreen(updates.getScreen()));
        }
        if (updates.getRowLabel() != null) {
            existing.setRowLabel(updates.getRowLabel());
        }
        if (updates.getSeatNumber() != null) {
            existing.setSeatNumber(updates.getSeatNumber());
        }
        if (updates.getSeatType() != null) {
            existing.setSeatType(updates.getSeatType());
        }
        return existing;
    }

    @Transactional(readOnly = true)
    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Seat> getSeatsByScreenId(Long screenId) {
        if (!screenRepository.existsById(screenId)) {
            throw new EntityNotFoundException("Screen not found with id: " + screenId);
        }
        return seatRepository.findByScreenIdOrderByRowLabelAscSeatNumberAsc(screenId);
    }

    private Screen resolveScreen(Screen screen) {
        if (screen == null || screen.getId() == null) {
            throw new EntityNotFoundException("Screen id is required");
        }
        return screenRepository.findById(screen.getId())
                .orElseThrow(() -> new EntityNotFoundException("Screen not found with id: " + screen.getId()));
    }
}
