package com.example.bookmyshow.services;

import com.example.bookmyshow.entities.Venue;
import com.example.bookmyshow.repositories.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;

    @Transactional
    public Venue addVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    @Transactional
    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new EntityNotFoundException("Venue not found with id: " + id);
        }
        venueRepository.deleteById(id);
    }

    @Transactional
    public Venue editVenue(Long id, Venue updates) {
        Venue existing = venueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + id));

        if (updates.getAddress() != null) {
            existing.setAddress(updates.getAddress());
        }
        if (updates.getSeats() != null) {
            existing.setSeats(updates.getSeats());
        }
        if (updates.getType() != null) {
            existing.setType(updates.getType());
        }
        return existing;
    }

    @Transactional(readOnly = true)
    public Venue getVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }
}
