package com.ivanfranchin.movieapi.service;

import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.rest.dto.SearchResponse;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Optional<Movie> getMovie(String imdb);

    List<Movie> getMovies();

    Movie validateAndGetMovie(String imdb);

    SearchResponse searchMovies(String title);

    Movie saveMovie(Movie movie);
}
