package com.ivanfranchin.movieapi.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OmdbResponse {

    @JsonProperty("imdbID")
    private String imdb;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Poster")
    private String posterUrl;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Released")
    private String released;

    private String imdbRating;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Runtime")
    private String runtime;

    @JsonProperty("Director")
    private String director;

    @JsonProperty("Writer")
    private String writer;

    @JsonProperty("Actors")
    private String actors;

    @JsonProperty("Plot")
    private String plot;

    @JsonProperty("Language")
    private String language;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Awards")
    private String awards;

    @JsonProperty("Response")
    private String response;

    public OmdbResponse(String response) {
        this.response = response;
    }
}
