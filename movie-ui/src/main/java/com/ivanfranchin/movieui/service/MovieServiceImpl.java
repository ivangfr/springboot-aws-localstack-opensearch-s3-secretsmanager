package com.ivanfranchin.movieui.service;

import com.ivanfranchin.movieui.client.MovieApiClient;
import com.ivanfranchin.movieui.controller.SearchResponse;
import com.ivanfranchin.movieui.model.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieApiClient movieApiClient;

    @Override
    public Optional<Movie> getMovie(String imdb) {
        try {
            Movie movie = movieApiClient.getMovie(imdb);
            return Optional.of(movie);
        } catch (Exception e) {
            log.error("An exception happened while getting movie imdb '{}' from movie-api. Error message: {}", imdb, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Movie> getMovies() {
        return searchMovies(null);
    }

    @Override
    public List<Movie> searchMovies(String title) {
        try {
            return getMovieList(movieApiClient.searchMovies(title == null ? "" : title));
        } catch (Exception e) {
            log.error("An exception happened while searching for '{}' in movie-api. Error message: {}", title, e.getMessage());
            return List.of();
        }
    }

    private List<Movie> getMovieList(SearchResponse searchResponse) {
        if (searchResponse.hits() == null) {
            return Collections.emptyList();
        }
        return searchResponse.hits()
                .stream()
                .map(SearchResponse.Hit::source)
                .map(Movie::from)
                .collect(Collectors.toList());
    }
}
