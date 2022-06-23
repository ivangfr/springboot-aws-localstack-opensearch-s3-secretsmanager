package com.mycompany.movieapi.service;

import com.mycompany.movieapi.rest.SearchMovieResponse;

import java.util.Map;
import java.util.Optional;

public interface MovieService {

    Optional<Map<String, Object>> getMovie(String imdb);

    Map<String, Object> validateAndGetMovie(String imdb);

    SearchMovieResponse searchMovies(String title);

    String saveMovie(Map<String, Object> source);
}
