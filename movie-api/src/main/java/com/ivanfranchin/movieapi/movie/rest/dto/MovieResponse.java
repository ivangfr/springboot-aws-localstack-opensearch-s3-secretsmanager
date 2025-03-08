package com.ivanfranchin.movieapi.movie.rest.dto;

import com.ivanfranchin.movieapi.movie.model.Movie;

public record MovieResponse(
        String imdb, String title, String poster, String year, String released, String imdbRating,
        String genre, String runtime, String director, String writer, String actors, String plot,
        String language, String country, String awards, Long createdAt) {

    public static MovieResponse from(Movie movie) {
        return new MovieResponse(
                movie.getImdb(),
                movie.getTitle(),
                movie.getPoster(),
                movie.getYear(),
                movie.getReleased(),
                movie.getImdbRating(),
                movie.getGenre(),
                movie.getRuntime(),
                movie.getDirector(),
                movie.getWriter(),
                movie.getActors(),
                movie.getPlot(),
                movie.getLanguage(),
                movie.getCountry(),
                movie.getAwards(),
                movie.getCreatedAt()
        );
    }
}
