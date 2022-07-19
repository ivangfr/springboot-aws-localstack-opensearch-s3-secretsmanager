package com.ivanfranchin.movieui.client;

import com.ivanfranchin.movieui.controller.SearchResponse;
import com.ivanfranchin.movieui.model.Movie;
import com.ivanfranchin.movieui.property.MovieApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class MovieApiClient {

    private final RestTemplate restTemplate;
    private final MovieApiProperties movieApiProperties;

    public Optional<Movie> getMovie(String imdb) {
        String url = String.format("%s/%s/%s", movieApiProperties.getUrl(), "/api/movies", imdb);
        try {
            ResponseEntity<Movie> responseEntity = restTemplate.getForEntity(url, Movie.class);
            return responseEntity.getBody() != null ? Optional.of(responseEntity.getBody()) : Optional.empty();
        } catch (Exception e) {
            log.error("An exception happened while getting movie imdb '{}' from movie-api. Error message: {}", imdb, e.getMessage());
            return Optional.empty();
        }
    }

    public SearchResponse searchMovies(String title) {
        String url = movieApiProperties.getUrl() + "/api/movies";
        if (title != null) {
            url = String.format("%s?title=%s", url, title);
        }
        try {
            ResponseEntity<SearchResponse> responseEntity = restTemplate.getForEntity(url, SearchResponse.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("An exception happened while searching for '{}' in movie-api. Error message: {}", title, e.getMessage());
            return SEARCH_MOVIE_RESPONSE_EMPTY;
        }
    }

    private static final SearchResponse SEARCH_MOVIE_RESPONSE_EMPTY = new SearchResponse();
}
