package com.example.movies.service;

import com.example.movies.dto.IntervalResponseDTO;
import com.example.movies.dto.MovieDTO;
import com.example.movies.dto.ProducerIntervalDTO;
import com.example.movies.dto.StudioWinCountDTO;
import com.example.movies.dto.YearWinnerCountDTO;
import com.example.movies.entity.Movie;
import com.example.movies.repository.MovieRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import jakarta.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostConstruct
    public void init() throws Exception {
        ClassPathResource resource = new ClassPathResource("movielist.csv");

        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .withSkipLines(1) // já pula o cabeçalho
                    .build();

            List<Movie> movies = new ArrayList<>();

            for (String[] record : csvReader.readAll()) {
                if (record.length < 5) continue; // ignora linhas inválidas

                Movie movie = new Movie();
                movie.setYear(parseYear(record[0]));
                movie.setTitle(record[1].trim());
                movie.setStudios(record[2].trim());
                movie.setProducers(record[3].trim());
                movie.setWinner(parseWinner(record[4]));
                movies.add(movie);
            }

            movieRepository.saveAll(movies); // salva em lote
        }
    }

    private int parseYear(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0; 
        }
    }

    private boolean parseWinner(String value) {
        if (value == null) return false;
        return value.trim().equalsIgnoreCase("yes") || value.trim().equalsIgnoreCase("true") || value.trim().equals("1");
    }

    public IntervalResponseDTO calculateIntervals() {
        List<Movie> winners = movieRepository.findByWinnerTrue();

        Map<String, List<Integer>> producerWins = new HashMap<>();
        for (Movie m : winners) {
            String[] producers = m.getProducers().split(",| and ");
            for (String p : producers) {
                String producer = p.trim();
                if (!producer.isEmpty()) {
                    producerWins.computeIfAbsent(producer, k -> new ArrayList<>()).add(m.getYear());
                }
            }
        }

        List<ProducerIntervalDTO> intervals = new ArrayList<>();
        producerWins.forEach((producer, yearsList) -> {
            List<Integer> years = yearsList.stream().sorted().toList();
            for (int i = 1; i < years.size(); i++) {
                int prev = years.get(i - 1);
                int next = years.get(i);
                intervals.add(new ProducerIntervalDTO(producer, next - prev, prev, next));
            }
        });
        

        int min = intervals.stream().mapToInt(ProducerIntervalDTO::getInterval).min().orElse(0);
        int max = intervals.stream().mapToInt(ProducerIntervalDTO::getInterval).max().orElse(0);

        Map<String, List<ProducerIntervalDTO>> result = new HashMap<>();
        result.put("min", intervals.stream().filter(i -> i.getInterval() == min).toList());
        result.put("max", intervals.stream().filter(i -> i.getInterval() == max).toList());

        return new IntervalResponseDTO(
                intervals.stream().filter(i -> i.getInterval() == min).toList(),
                intervals.stream().filter(i -> i.getInterval() == max).toList()
            );
        }

    
    public List<YearWinnerCountDTO> getYearsWithMultipleWinners() {
        return movieRepository.findAll().stream()
                .collect(Collectors.groupingBy(Movie::getYear, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(e -> new YearWinnerCountDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(YearWinnerCountDTO::getWinnerCount).reversed())
                .toList();
    }

    public List<StudioWinCountDTO> getStudiosWithWinCount() {
        return movieRepository.findAll().stream()
                .filter(Movie::isWinner)
                .collect(Collectors.groupingBy(Movie::getStudios, Collectors.counting()))
                .entrySet().stream()
                .map(e -> new StudioWinCountDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(StudioWinCountDTO::winCount).reversed())
                .toList();
    }

    public List<MovieDTO> findAll(Integer year, Boolean winner) {
        return movieRepository.findAll().stream()
                .filter(m -> (year == null || m.getYear() == year))
                .filter(m -> (winner == null || m.isWinner() == winner))
                .map(m -> new MovieDTO(m.getYear(), m.getTitle(), m.getStudios(), m.isWinner()))
                .toList();
    }


    public List<MovieDTO> getWinnersByYear(int year) {
        return movieRepository.findByWinnerTrue().stream()
                .filter(m -> m.getYear() == year)
                .map(m -> new MovieDTO(m.getYear(), m.getTitle(), m.getStudios(), m.isWinner()))
                .toList();
    }


}
