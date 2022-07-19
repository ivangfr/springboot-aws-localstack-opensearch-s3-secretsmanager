package com.ivanfranchin.movieapi.service;

import org.opensearch.search.SearchHits;

import java.util.Map;

public interface OpenSearchService {

    Map<String, Object> getMovie(String imdb);

    SearchHits searchMovies(String title);

    Map<String, Object> saveMovie(Map<String, Object> movieMap);
}
