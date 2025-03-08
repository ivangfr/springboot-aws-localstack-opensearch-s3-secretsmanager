package com.ivanfranchin.movieapi.service;

import com.ivanfranchin.movieapi.exception.MovieNotFoundException;
import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.rest.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private final OpenSearchService openSearchService;

    @Override
    public Optional<Movie> getMovie(String imdb) {
        try {
            return Optional.of(Movie.from(openSearchService.getMovie(imdb)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Movie> getMovies() {
        return getMovieList(searchMovies(null));
    }

    @Override
    public Movie validateAndGetMovie(String imdb) {
        return getMovie(imdb)
                .orElseThrow(() -> new MovieNotFoundException(String.format("Movie with imdb '%s' not found", imdb)));
    }

    @Override
    public SearchResponse searchMovies(String title) {
        try {
            return SearchResponse.from(openSearchService.searchMovies(title));
        } catch (Exception e) {
            return createSearchResponseError(e.getMessage());
        }
    }

    @Override
    public Movie saveMovie(Movie movie) {
        Map<String, Object> movieMap = openSearchService.saveMovie(Movie.toMap(movie));
        return Movie.from(movieMap);
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

    private SearchResponse createSearchResponseError(String errorMessage) {
        return new SearchResponse(new SearchResponse.Error(errorMessage));
    }
}
