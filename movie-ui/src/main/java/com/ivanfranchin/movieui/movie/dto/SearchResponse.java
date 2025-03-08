package com.ivanfranchin.movieui.movie.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ivanfranchin.movieui.movie.model.Movie;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(Include.NON_NULL)
public record SearchResponse(List<Hit> hits, Error error) {

    public SearchResponse() {
        this(null, null);
    }

    public record Hit(String index, String id, Float score, Map<String, Object> source) {
    }

    public record Error(String message) {
    }

    public static List<Movie> toMovieList(SearchResponse searchResponse) {
        if (searchResponse.hits() == null) {
            return Collections.emptyList();
        }
        return searchResponse.hits()
                .stream()
                .map(SearchResponse.Hit::source)
                .map(Movie::from)
                .collect(Collectors.toList());
    }
}
