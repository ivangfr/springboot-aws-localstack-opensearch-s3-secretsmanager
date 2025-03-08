package com.ivanfranchin.movieui.movie;

import com.ivanfranchin.movieui.client.MovieApiClient;
import com.ivanfranchin.movieui.movie.dto.SearchResponse;
import com.ivanfranchin.movieui.movie.model.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieApiClient movieApiClient;

    public Optional<Movie> getMovie(String imdb) {
        try {
            Movie movie = movieApiClient.getMovie(imdb);
            return Optional.of(movie);
        } catch (Exception e) {
            log.error("An exception happened while getting movie imdb '{}' from movie-api. Error message: {}", imdb, e.getMessage());
            return Optional.empty();
        }
    }

    public List<Movie> getMovies() {
        return searchMovies(null);
    }

    public List<Movie> searchMovies(String title) {
        try {
            return SearchResponse.toMovieList(movieApiClient.searchMovies(title == null ? "" : title));
        } catch (Exception e) {
            log.error("An exception happened while searching for '{}' in movie-api. Error message: {}", title, e.getMessage());
            return List.of();
        }
    }
}
