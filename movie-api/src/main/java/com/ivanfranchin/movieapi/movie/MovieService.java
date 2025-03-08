package com.ivanfranchin.movieapi.movie;

import com.ivanfranchin.movieapi.aws.OpenSearchService;
import com.ivanfranchin.movieapi.movie.exception.MovieNotFoundException;
import com.ivanfranchin.movieapi.movie.model.Movie;
import com.ivanfranchin.movieapi.movie.rest.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieService {

    private final OpenSearchService openSearchService;

    public Optional<Movie> getMovie(String imdb) {
        try {
            return Optional.of(Movie.from(openSearchService.getMovie(imdb)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Movie> getMovies() {
        return SearchResponse.toMovieList(searchMovies(null));
    }

    public Movie validateAndGetMovie(String imdb) {
        return getMovie(imdb)
                .orElseThrow(() -> new MovieNotFoundException(String.format("Movie with imdb '%s' not found", imdb)));
    }

    public SearchResponse searchMovies(String title) {
        try {
            return SearchResponse.from(openSearchService.searchMovies(title));
        } catch (Exception e) {
            return SearchResponse.createError(e.getMessage());
        }
    }

    public Movie saveMovie(Movie movie) {
        Map<String, Object> movieMap = openSearchService.saveMovie(Movie.toMap(movie));
        return Movie.from(movieMap);
    }
}
