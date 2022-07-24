package com.ivanfranchin.movieapi.mapper;

import com.ivanfranchin.movieapi.client.OmdbResponse;
import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.rest.dto.AddMovieRequest;
import com.ivanfranchin.movieapi.rest.dto.MovieResponse;
import com.ivanfranchin.movieapi.rest.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.opensearch.search.SearchHit;
import org.opensearch.search.SearchHits;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class MovieMapper {

    public Movie toMovie(Map<String, Object> movieMap) {
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

    public Map<String, Object> toMovieMap(Movie movie) {
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

    public Movie toMovie(AddMovieRequest addMovieRequest) {
        return new Movie(
                addMovieRequest.getImdb(),
                addMovieRequest.getTitle(),
                null,
                addMovieRequest.getPosterUrl().toString(),
                addMovieRequest.getYear(),
                addMovieRequest.getReleased(),
                addMovieRequest.getImdbRating(),
                addMovieRequest.getGenre(),
                addMovieRequest.getRuntime(),
                addMovieRequest.getDirector(),
                addMovieRequest.getWriter(),
                addMovieRequest.getActors(),
                addMovieRequest.getPlot(),
                addMovieRequest.getLanguage(),
                addMovieRequest.getCountry(),
                addMovieRequest.getAwards(),
                Clock.systemDefaultZone().millis()
        );
    }

    public Movie toMovie(OmdbResponse omdbResponse) {
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

    public SearchResponse toSearchResponse(SearchHits searchHits) {
        SearchResponse searchResponse = new SearchResponse();
        List<SearchResponse.Hit> hits = new ArrayList<>();
        for (SearchHit searchHit : searchHits.getHits()) {
            SearchResponse.Hit hit = new SearchResponse.Hit();
            hit.setIndex(searchHit.getIndex());
            hit.setId(searchHit.getId());
            hit.setScore(searchHit.getScore());
            hit.setSource(searchHit.getSourceAsMap());
            hits.add(hit);
        }
        searchResponse.setHits(hits);
        return searchResponse;
    }

    public MovieResponse toMovieResponse(Movie movie) {
        return new MovieResponse(
                movie.getImdb(),
                movie.getTitle(),
                movie.getPoster(),
                movie.getYear(),
                movie.getReleased(),
                movie.getImdbRating(),
                movie.getGenre(),
                movie.getRuntime(),
                movie.getDirector(),
                movie.getWriter(),
                movie.getActors(),
                movie.getPlot(),
                movie.getLanguage(),
                movie.getCountry(),
                movie.getAwards(),
                movie.getCreatedAt()
        );
    }
}
