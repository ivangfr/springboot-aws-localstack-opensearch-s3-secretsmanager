package com.mycompany.movieapi.mapper;

import com.mycompany.movieapi.client.OmdbResponse;
import com.mycompany.movieapi.model.Movie;
import com.mycompany.movieapi.rest.AddMovieRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class MovieMapper {

    public Movie toMovie(Map<String, Object> source) {
        return new Movie(
                String.valueOf(source.get("imdb")),
                String.valueOf(source.get("title")),
                String.valueOf(source.get("poster")),
                String.valueOf(source.get("year")),
                String.valueOf(source.get("released")),
                String.valueOf(source.get("imdbRating")),
                String.valueOf(source.get("genre")),
                String.valueOf(source.get("runtime")),
                String.valueOf(source.get("director")),
                String.valueOf(source.get("writer")),
                String.valueOf(source.get("actors")),
                String.valueOf(source.get("plot")),
                String.valueOf(source.get("language")),
                String.valueOf(source.get("country")),
                String.valueOf(source.get("awards"))
        );
    }

    public Map<String, Object> toSource(Movie movie) {
        Map<String, Object> source = new HashMap<>();
        source.put("imdb", movie.getImdb());
        source.put("title", movie.getTitle());
        source.put("poster", movie.getPoster());
        source.put("year", movie.getYear());
        source.put("released", movie.getReleased());
        source.put("imdbRating", movie.getImdbRating());
        source.put("genre", movie.getGenre());
        source.put("runtime", movie.getRuntime());
        source.put("director", movie.getDirector());
        source.put("writer", movie.getWriter());
        source.put("actors", movie.getActors());
        source.put("plot", movie.getPlot());
        source.put("language", movie.getLanguage());
        source.put("country", movie.getCountry());
        source.put("awards", movie.getAwards());
        return source;
    }

    public Map<String, Object> toSource(AddMovieRequest addMovieRequest) {
        Map<String, Object> source = new HashMap<>();
        source.put("imdb", addMovieRequest.getImdb());
        source.put("title", addMovieRequest.getTitle());
        source.put("posterUrl", addMovieRequest.getPosterUrl());
        source.put("year", addMovieRequest.getYear());
        source.put("released", addMovieRequest.getReleased());
        source.put("imdbRating", addMovieRequest.getImdbRating());
        source.put("genre", addMovieRequest.getGenre());
        source.put("runtime", addMovieRequest.getRuntime());
        source.put("director", addMovieRequest.getDirector());
        source.put("writer", addMovieRequest.getWriter());
        source.put("actors", addMovieRequest.getActors());
        source.put("plot", addMovieRequest.getPlot());
        source.put("language", addMovieRequest.getLanguage());
        source.put("country", addMovieRequest.getCountry());
        source.put("awards", addMovieRequest.getAwards());
        return source;
    }

    public Map<String, Object> toSource(OmdbResponse omdbResponse) {
        Map<String, Object> source = new HashMap<>();
        source.put("imdb", omdbResponse.getImdb());
        source.put("title", omdbResponse.getTitle());
        source.put("posterUrl", omdbResponse.getPosterUrl());
        source.put("year", omdbResponse.getYear());
        source.put("released", omdbResponse.getReleased());
        source.put("imdbRating", omdbResponse.getImdbRating());
        source.put("genre", omdbResponse.getGenre());
        source.put("runtime", omdbResponse.getRuntime());
        source.put("director", omdbResponse.getDirector());
        source.put("writer", omdbResponse.getWriter());
        source.put("actors", omdbResponse.getActors());
        source.put("plot", omdbResponse.getPlot());
        source.put("language", omdbResponse.getLanguage());
        source.put("country", omdbResponse.getCountry());
        source.put("awards", omdbResponse.getAwards());
        return source;
    }
}
