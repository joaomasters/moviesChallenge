package com.example.movies.controller;

import com.example.movies.entity.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/movies";
    }

    @Test
    void shouldLoadMoviesOnStartup() {
        ResponseEntity<Movie[]> response =
                restTemplate.getForEntity(baseUrl(), Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()[0].getTitle()).isNotBlank();
    }

    @Test
    void shouldReturnIntervals() {
        ResponseEntity<Map> response =
                restTemplate.getForEntity(baseUrl() + "/intervals", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKeys("min", "max");
    }

    @Test
    void shouldReturnYearsWithMultipleWinners() {
        ResponseEntity<List> response =
                restTemplate.getForEntity(baseUrl() + "/years-with-multiple-winners", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(((Map) response.getBody().get(0))).containsKeys("year", "winnerCount");
    }

    @Test
    void shouldReturnStudiosWithWinCount() {
        ResponseEntity<List> response =
                restTemplate.getForEntity(baseUrl() + "/studios-with-win-count", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(((Map) response.getBody().get(0))).containsKeys("studio", "winCount");
    }

    @Test
    void shouldReturnWinnersByYear() {
        int testYear = 1980; // ano quE que existe no CSV
        ResponseEntity<Movie[]> response =
                restTemplate.getForEntity(baseUrl() + "/winners-by-year/" + testYear, Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    
    @Test
    void shouldReturnIntervalsWithExpectedFields() {
        ResponseEntity<Map> response =
                restTemplate.getForEntity(baseUrl() + "/intervals", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKeys("min", "max");

        List<Map<String, Object>> minList = (List<Map<String, Object>>) response.getBody().get("min");
        List<Map<String, Object>> maxList = (List<Map<String, Object>>) response.getBody().get("max");

        assertThat(minList).allSatisfy(item -> {
            assertThat(item).containsKeys("producer", "interval", "previousWin", "followingWin");
        });

        assertThat(maxList).allSatisfy(item -> {
            assertThat(item).containsKeys("producer", "interval", "previousWin", "followingWin");
        });
    }
    
    @Test
    void shouldReturnEmptyListWhenNoWinnersInYear() {
        int futureYear = 3000;
        ResponseEntity<Movie[]> response =
                restTemplate.getForEntity(baseUrl() + "/winners-by-year/" + futureYear, Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void shouldReturnStudiosOrderedByWinCount() {
        ResponseEntity<List> response =
                restTemplate.getForEntity(baseUrl() + "/studios-with-win-count", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Map<String, Object>> studios = (List<Map<String, Object>>) response.getBody();

        // Verifica ordem decrescente
        for (int i = 1; i < studios.size(); i++) {
            long prev = (long) studios.get(i - 1).get("winCount");
            long curr = (long) studios.get(i).get("winCount");
            assertThat(prev).isGreaterThanOrEqualTo(curr);
        }
    }

    @Test
    void shouldReturnValidIntervals() {
        ResponseEntity<Map> response =
                restTemplate.getForEntity(baseUrl() + "/intervals", Map.class);

        List<Map<String, Object>> minList = (List<Map<String, Object>>) response.getBody().get("min");
        List<Map<String, Object>> maxList = (List<Map<String, Object>>) response.getBody().get("max");

        assertThat(minList).allSatisfy(item -> {
            int interval = (int) item.get("interval");
            int prev = (int) item.get("previousWin");
            int next = (int) item.get("followingWin");

            assertThat(interval).isGreaterThanOrEqualTo(1);
            assertThat(prev).isLessThan(next);
        });

        assertThat(maxList).allSatisfy(item -> {
            int interval = (int) item.get("interval");
            int prev = (int) item.get("previousWin");
            int next = (int) item.get("followingWin");

            assertThat(interval).isGreaterThanOrEqualTo(1);
            assertThat(prev).isLessThan(next);
        });
    }


}
