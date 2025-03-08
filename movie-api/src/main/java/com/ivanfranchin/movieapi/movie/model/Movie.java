package com.ivanfranchin.movieapi.movie.model;

import com.ivanfranchin.movieapi.omdb.OmdbResponse;
import com.ivanfranchin.movieapi.movie.rest.dto.AddMovieRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

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

    public static Movie from(AddMovieRequest addMovieRequest) {
        return new Movie(
                addMovieRequest.imdb(),
                addMovieRequest.title(),
                null,
                addMovieRequest.posterUrl().toString(),
                addMovieRequest.year(),
                addMovieRequest.released(),
                addMovieRequest.imdbRating(),
                addMovieRequest.genre(),
                addMovieRequest.runtime(),
                addMovieRequest.director(),
                addMovieRequest.writer(),
                addMovieRequest.actors(),
                addMovieRequest.plot(),
                addMovieRequest.language(),
                addMovieRequest.country(),
                addMovieRequest.awards(),
                Clock.systemDefaultZone().millis()
        );
    }

    public static Movie from(OmdbResponse omdbResponse) {
        return new Movie(
                omdbResponse.getImdb(),
                omdbResponse.getTitle(),
                null,
                omdbResponse.getPosterUrl(),
                omdbResponse.getYear(),
                omdbResponse.getReleased(),
                omdbResponse.getImdbRating(),
                omdbResponse.getGenre(),
                omdbResponse.getRuntime(),
                omdbResponse.getDirector(),
                omdbResponse.getWriter(),
                omdbResponse.getActors(),
                omdbResponse.getPlot(),
                omdbResponse.getLanguage(),
                omdbResponse.getCountry(),
                omdbResponse.getAwards(),
                Clock.systemDefaultZone().millis()
        );
    }

    public static Movie from(Map<String, Object> movieMap) {
        return new Movie(
                movieMap.get("imdb") != null ? String.valueOf(movieMap.get("imdb")) : null,
                movieMap.get("title") != null ? String.valueOf(movieMap.get("title")) : null,
                movieMap.get("poster") != null ? String.valueOf(movieMap.get("poster")) : null,
                movieMap.get("posterUrl") != null ? String.valueOf(movieMap.get("posterUrl")) : null,
                movieMap.get("year") != null ? String.valueOf(movieMap.get("year")) : null,
                movieMap.get("released") != null ? String.valueOf(movieMap.get("released")) : null,
                movieMap.get("imdbRating") != null ? String.valueOf(movieMap.get("imdbRating")) : null,
                movieMap.get("genre") != null ? String.valueOf(movieMap.get("genre")) : null,
                movieMap.get("runtime") != null ? String.valueOf(movieMap.get("runtime")) : null,
                movieMap.get("director") != null ? String.valueOf(movieMap.get("director")) : null,
                movieMap.get("writer") != null ? String.valueOf(movieMap.get("writer")) : null,
                movieMap.get("actors") != null ? String.valueOf(movieMap.get("actors")) : null,
                movieMap.get("plot") != null ? String.valueOf(movieMap.get("plot")) : null,
                movieMap.get("language") != null ? String.valueOf(movieMap.get("language")) : null,
                movieMap.get("country") != null ? String.valueOf(movieMap.get("country")) : null,
                movieMap.get("awards") != null ? String.valueOf(movieMap.get("awards")) : null,
                movieMap.get("createdAt") != null ? Long.valueOf(String.valueOf(movieMap.get("createdAt"))) : null
        );
    }

    public static Map<String, Object> toMap(Movie movie) {
        Map<String, Object> movieMap = new HashMap<>();
        movieMap.put("imdb", movie.getImdb());
        movieMap.put("title", movie.getTitle());
        movieMap.put("poster", movie.getPoster());
        movieMap.put("posterUrl", movie.getPosterUrl());
        movieMap.put("year", movie.getYear());
        movieMap.put("released", movie.getReleased());
        movieMap.put("imdbRating", movie.getImdbRating());
        movieMap.put("genre", movie.getGenre());
        movieMap.put("runtime", movie.getRuntime());
        movieMap.put("director", movie.getDirector());
        movieMap.put("writer", movie.getWriter());
        movieMap.put("actors", movie.getActors());
        movieMap.put("plot", movie.getPlot());
        movieMap.put("language", movie.getLanguage());
        movieMap.put("country", movie.getCountry());
        movieMap.put("awards", movie.getAwards());
        movieMap.put("createdAt", movie.getCreatedAt());
        return movieMap;
    }
}
