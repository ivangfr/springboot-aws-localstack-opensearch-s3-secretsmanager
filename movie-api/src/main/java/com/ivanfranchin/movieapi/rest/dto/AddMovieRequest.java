package com.ivanfranchin.movieapi.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.net.URL;

public record AddMovieRequest(
        @Schema(example = "tt0106489") @NotBlank String imdb,
        @Schema(example = "A Bronx Tale") @NotBlank String title,
        @Schema(example = "https://m.media-amazon.com/images/M/MV5BMTczOTczNjE3Ml5BMl5BanBnXkFtZTgwODEzMzg5MTI@._V1_SX300.jpg") URL posterUrl,
        @Schema(example = "1993") String year, @Schema(example = "01 Oct 1993") String released,
        @Schema(example = "7.8") String imdbRating,
        @Schema(example = "Crime, Drama") String genre,
        @Schema(example = "121 min") String runtime,
        @Schema(example = "Robert De Niro") String director,
        @Schema(example = "Chazz Palminteri") String writer,
        @Schema(example = "Robert De Niro, Chazz Palminteri, Lillo Brancato") String actors,
        @Schema(example = "A father becomes worried when a local gangster befriends his son in the Bronx in the 1960s.") String plot,
        @Schema(example = "English, Italian") String language,
        @Schema(example = "United States") String country,
        @Schema(example = "1 win & 3 nominations") String awards) {
}
