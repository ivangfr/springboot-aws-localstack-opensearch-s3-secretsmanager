package com.ivanfranchin.movieapi.client;

import com.ivanfranchin.movieapi.properties.OmdbApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Component
public class OmdbApiClient {

    private final RestTemplate restTemplate;
    private final OmdbApiProperties omdbApiProperties;

    public OmdbResponse getMovieByTitle(String title) {
        String url = String.format("%s/?apikey=%s&t=%s",
                omdbApiProperties.getUrl(), omdbApiProperties.getApiKey(), title);
        return restTemplate.getForEntity(url, OmdbResponse.class).getBody();
    }
}
