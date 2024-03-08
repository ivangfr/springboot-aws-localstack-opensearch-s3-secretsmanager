package com.ivanfranchin.movieui.client;

import com.ivanfranchin.movieui.controller.SearchResponse;
import com.ivanfranchin.movieui.model.Movie;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/movies")
public interface MovieApiClient {

    @GetExchange("/{imdb}")
    Movie getMovie(@PathVariable String imdb);

    @GetExchange
    SearchResponse searchMovies(@RequestParam(required = false) String title);
}
