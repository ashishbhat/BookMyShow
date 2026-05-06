package com.example.bookmyshow.services;

import com.example.bookmyshow.entities.Screen;
import com.example.bookmyshow.entities.Venue;
import com.example.bookmyshow.repositories.ScreenRepository;
import com.example.bookmyshow.repositories.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final VenueRepository venueRepository;

    @Transactional
    public Screen addScreen(Screen screen) {
        screen.setVenue(resolveVenue(screen.getVenue()));
        return screenRepository.save(screen);
    }

    @Transactional
    public void deleteScreen(Long id) {
        if (!screenRepository.existsById(id)) {
            throw new EntityNotFoundException("Screen not found with id: " + id);
        }
        screenRepository.deleteById(id);
    }

    @Transactional
    public Screen editScreen(Long id, Screen updates) {
        Screen existing = screenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Screen not found with id: " + id));

        if (updates.getVenue() != null) {
            existing.setVenue(resolveVenue(updates.getVenue()));
        }
        if (updates.getType() != null) {
            existing.setType(updates.getType());
        }
        return existing;
    }

    @Transactional(readOnly = true)
    public Screen getScreenById(Long id) {
        return screenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Screen not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Screen> getAllScreens() {
        return screenRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Screen> getScreensByVenueId(Long venueId) {
        if (!venueRepository.existsById(venueId)) {
            throw new EntityNotFoundException("Venue not found with id: " + venueId);
        }
        return screenRepository.findByVenueId(venueId);
    }

    private Venue resolveVenue(Venue venue) {
        if (venue == null || venue.getId() == null) {
            throw new EntityNotFoundException("Venue id is required");
        }
        return venueRepository.findById(venue.getId())
                .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + venue.getId()));
    }
}
