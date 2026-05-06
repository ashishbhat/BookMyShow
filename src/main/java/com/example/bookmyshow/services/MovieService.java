package com.example.bookmyshow.services;

import com.example.bookmyshow.entities.Movie;
import com.example.bookmyshow.repositories.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    @Transactional
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new EntityNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    @Transactional
    public Movie editMovie(Long id, Movie updates) {
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + id));

        if (updates.getName() != null) {
            existing.setName(updates.getName());
        }
        if (updates.getDirector() != null) {
            existing.setDirector(updates.getDirector());
        }
        if (updates.getRating() != null) {
            existing.setRating(updates.getRating());
        }
        if (updates.getUaRating() != null) {
            existing.setUaRating(updates.getUaRating());
        }
        return existing;
    }

    @Transactional(readOnly = true)
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Movie> searchMovies(String query) {
        if (query == null || query.isBlank()) {
            return movieRepository.findAll();
        }
        return movieRepository.findByNameContainingIgnoreCaseOrderByNameAsc(query.trim());
    }
}
