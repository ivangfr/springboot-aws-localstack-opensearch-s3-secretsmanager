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

    public Movie toMovie(Map<String, Object> movieMap) {
        return new Movie(
                movieMap.get("imdb") != null ? String.valueOf(movieMap.get("imdb")) : null,
                movieMap.get("title") != null ? String.valueOf(movieMap.get("title")) : null,
                movieMap.get("poster") != null ? String.valueOf(movieMap.get("poster")) : null,
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
                movieMap.get("awards") != null ? String.valueOf(movieMap.get("awards")) : null
        );
    }

    public Map<String, Object> toMovieMap(Movie movie) {
        Map<String, Object> movieMap = new HashMap<>();
        movieMap.put("imdb", movie.getImdb());
        movieMap.put("title", movie.getTitle());
        movieMap.put("poster", movie.getPoster());
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
        return movieMap;
    }

    public Map<String, Object> toMovieMap(AddMovieRequest addMovieRequest) {
        Map<String, Object> movieMap = new HashMap<>();
        movieMap.put("imdb", addMovieRequest.getImdb());
        movieMap.put("title", addMovieRequest.getTitle());
        movieMap.put("posterUrl", addMovieRequest.getPosterUrl());
        movieMap.put("year", addMovieRequest.getYear());
        movieMap.put("released", addMovieRequest.getReleased());
        movieMap.put("imdbRating", addMovieRequest.getImdbRating());
        movieMap.put("genre", addMovieRequest.getGenre());
        movieMap.put("runtime", addMovieRequest.getRuntime());
        movieMap.put("director", addMovieRequest.getDirector());
        movieMap.put("writer", addMovieRequest.getWriter());
        movieMap.put("actors", addMovieRequest.getActors());
        movieMap.put("plot", addMovieRequest.getPlot());
        movieMap.put("language", addMovieRequest.getLanguage());
        movieMap.put("country", addMovieRequest.getCountry());
        movieMap.put("awards", addMovieRequest.getAwards());
        return movieMap;
    }

    public Map<String, Object> toMovieMap(OmdbResponse omdbResponse) {
        Map<String, Object> movieMap = new HashMap<>();
        movieMap.put("imdb", omdbResponse.getImdb());
        movieMap.put("title", omdbResponse.getTitle());
        movieMap.put("posterUrl", omdbResponse.getPosterUrl());
        movieMap.put("year", omdbResponse.getYear());
        movieMap.put("released", omdbResponse.getReleased());
        movieMap.put("imdbRating", omdbResponse.getImdbRating());
        movieMap.put("genre", omdbResponse.getGenre());
        movieMap.put("runtime", omdbResponse.getRuntime());
        movieMap.put("director", omdbResponse.getDirector());
        movieMap.put("writer", omdbResponse.getWriter());
        movieMap.put("actors", omdbResponse.getActors());
        movieMap.put("plot", omdbResponse.getPlot());
        movieMap.put("language", omdbResponse.getLanguage());
        movieMap.put("country", omdbResponse.getCountry());
        movieMap.put("awards", omdbResponse.getAwards());
        return movieMap;
    }
}
