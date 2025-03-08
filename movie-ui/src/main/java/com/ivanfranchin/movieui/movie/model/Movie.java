package com.ivanfranchin.movieui.movie.model;

import java.util.Map;

public record Movie(
        String imdb, String title, String poster, String year, String released, String imdbRating,
        String genre, String runtime, String director, String writer, String actors, String plot,
        String language, String country, String awards, Long createdAt) {

    public static Movie from(Map<String, Object> movieMap) {
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
                movieMap.get("awards") != null ? String.valueOf(movieMap.get("awards")) : null,
                movieMap.get("createdAt") != null ? Long.valueOf(String.valueOf(movieMap.get("createdAt"))) : null
        );
    }
}
