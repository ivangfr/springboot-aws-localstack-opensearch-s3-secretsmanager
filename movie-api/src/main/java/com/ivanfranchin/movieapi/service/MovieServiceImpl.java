package com.ivanfranchin.movieapi.service;

import com.ivanfranchin.movieapi.exception.MovieNotFoundException;
import com.ivanfranchin.movieapi.mapper.MovieMapper;
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
    private final MovieMapper movieMapper;

    @Override
    public Optional<Movie> getMovie(String imdb) {
        try {
            return Optional.of(movieMapper.toMovie(openSearchService.getMovie(imdb)));
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
            return movieMapper.toSearchResponse(openSearchService.searchMovies(title));
        } catch (Exception e) {
            return createSearchResponseError(e.getMessage());
        }
    }

    @Override
    public Movie saveMovie(Movie movie) {
        Map<String, Object> movieMap = openSearchService.saveMovie(movieMapper.toMovieMap(movie));
        return movieMapper.toMovie(movieMap);
    }

    private List<Movie> getMovieList(SearchResponse searchResponse) {
        if (searchResponse.getHits() == null) {
            return Collections.emptyList();
        }
        return searchResponse.getHits()
                .stream()
                .map(SearchResponse.Hit::getSource)
                .map(movieMapper::toMovie)
                .collect(Collectors.toList());
    }

    private SearchResponse createSearchResponseError(String errorMessage) {
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setError(new SearchResponse.Error(errorMessage));
        return searchResponse;
    }
}
