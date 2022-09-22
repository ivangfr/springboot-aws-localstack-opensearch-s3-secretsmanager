package com.ivanfranchin.movieapi.rest.dto;

public record MovieResponse(String imdb, String title, String poster, String year, String released, String imdbRating,
                            String genre, String runtime, String director, String writer, String actors, String plot,
                            String language, String country, String awards, Long createdAt) {
}
