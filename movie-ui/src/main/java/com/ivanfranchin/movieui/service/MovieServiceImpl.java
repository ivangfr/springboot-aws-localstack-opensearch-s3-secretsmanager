package com.ivanfranchin.movieui.service;

import com.ivanfranchin.movieui.client.MovieApiClient;
import com.ivanfranchin.movieui.controller.SearchResponse;
import com.ivanfranchin.movieui.mapper.MovieMapper;
import com.ivanfranchin.movieui.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieApiClient movieApiClient;
    private final MovieMapper movieMapper;

    @Override
    public Optional<Movie> getMovie(String imdb) {
        return movieApiClient.getMovie(imdb);
    }

    @Override
    public List<Movie> getMovies() {
        return searchMovies(null);
    }

    @Override
    public List<Movie> searchMovies(String title) {
        return getMovieList(movieApiClient.searchMovies(title));
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
}
