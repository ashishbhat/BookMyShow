package com.example.bookmyshow.repositories;

import com.example.bookmyshow.entities.Listing;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    @EntityGraph(attributePaths = {"movie", "screen", "screen.venue"})
    List<Listing> findByMovieIdOrderByDateAscStartAsc(Long movieId);

    @EntityGraph(attributePaths = {"movie", "screen", "screen.venue"})
    List<Listing> findByMovieIdAndDateOrderByStartAsc(Long movieId, LocalDate date);
}
