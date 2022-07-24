package com.ivanfranchin.movieapi.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.net.URL;

@Data
public class AddMovieRequest {

    @Schema(example = "tt0106489")
    @NotBlank
    private String imdb;

    @Schema(example = "A Bronx Tale")
    @NotBlank
    private String title;

    @Schema(example = "https://m.media-amazon.com/images/M/MV5BMTczOTczNjE3Ml5BMl5BanBnXkFtZTgwODEzMzg5MTI@._V1_SX300.jpg")
    private URL posterUrl;

    @Schema(example = "1993")
    private String year;

    @Schema(example = "01 Oct 1993")
    private String released;

    @Schema(example = "7.8")
    private String imdbRating;

    @Schema(example = "Crime, Drama")
    private String genre;

    @Schema(example = "121 min")
    private String runtime;

    @Schema(example = "Robert De Niro")
    private String director;

    @Schema(example = "Chazz Palminteri")
    private String writer;

    @Schema(example = "Robert De Niro, Chazz Palminteri, Lillo Brancato")
    private String actors;

    @Schema(example = "A father becomes worried when a local gangster befriends his son in the Bronx in the 1960s.")
    private String plot;

    @Schema(example = "English, Italian")
    private String language;

    @Schema(example = "United States")
    private String country;

    @Schema(example = "1 win & 3 nominations")
    private String awards;
}
