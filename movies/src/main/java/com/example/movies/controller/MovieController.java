package com.example.movies.controller;

import com.example.movies.dto.IntervalResponseDTO;
import com.example.movies.dto.MovieDTO;
import com.example.movies.dto.ProducerIntervalDTO;
import com.example.movies.dto.StudioWinCountDTO;
import com.example.movies.dto.YearWinnerCountDTO;
import com.example.movies.entity.Movie;
import com.example.movies.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

 
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/intervals")
    public IntervalResponseDTO getIntervals() {
        return movieService.calculateIntervals();
    }

    @GetMapping
    public List<MovieDTO> getAllMovies(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Boolean winner) {
        return movieService.findAll(year, winner);
    }


    @GetMapping("/winners-by-year/{year}")
    public List<MovieDTO> getWinnersByYear(@PathVariable int year) {
        return movieService.getWinnersByYear(year);   
    }

    
    @GetMapping("/years-with-multiple-winners")
    public List<YearWinnerCountDTO> getYearsWithMultipleWinners() {
        return movieService.getYearsWithMultipleWinners();
    }

    @GetMapping("/studios-with-win-count")
    public List<StudioWinCountDTO> getStudiosWithWinCount() {
        return movieService.getStudiosWithWinCount();
    }


}
