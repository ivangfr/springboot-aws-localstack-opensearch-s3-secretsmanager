package com.ivanfranchin.movieui.service;

import com.ivanfranchin.movieui.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Optional<Movie> getMovie(String imdb);

    List<Movie> getMovies();

    List<Movie> searchMovies(String title);
}
