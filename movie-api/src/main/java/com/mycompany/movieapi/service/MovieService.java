package com.mycompany.movieapi.service;

import com.mycompany.movieapi.model.Movie;
import com.mycompany.movieapi.rest.SearchResponse;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Optional<Movie> getMovie(String imdb);

    List<Movie> getMovies();

    Movie validateAndGetMovie(String imdb);

    SearchResponse searchMovies(String title);

    Movie saveMovie(Movie movie);
}
