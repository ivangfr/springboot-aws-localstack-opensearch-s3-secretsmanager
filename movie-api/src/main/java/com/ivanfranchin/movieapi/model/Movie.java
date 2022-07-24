package com.ivanfranchin.movieapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Movie {

    private String imdb;
    private String title;
    private String poster;
    private String posterUrl;
    private String year;
    private String released;
    private String imdbRating;
    private String genre;
    private String runtime;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String language;
    private String country;
    private String awards;
    private Long createdAt;
}
