package com.ivanfranchin.movieapi.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface OmdbApiClient {

    @GetExchange
    OmdbResponse getMovieByTitle(@RequestParam String apiKey, @RequestParam(name = "t") String title);
}
