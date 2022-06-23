package com.mycompany.movieapi.client;

import com.mycompany.movieapi.property.OmdbApiProperties;
import com.mycompany.movieapi.service.SecretsManagerService;
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
    private final SecretsManagerService secretsManagerService;

    public OmdbResponse getMovieByImdb(String imdb) {
        try {
            String url = String.format("%s/?apikey=%s&i=%s",
                    omdbApiProperties.getUrl(), secretsManagerService.getSecret("omdbApiKey"), imdb);
            return restTemplate.getForEntity(url, OmdbResponse.class).getBody();
        } catch (Exception e) {
            log.error("Unable to get information about the movie with imdb '{}' due to the following exception: {}", imdb, e.getMessage());
            return MOVIE_INFO_EMPTY;
        }
    }

    public OmdbResponse getMovieByTitle(String title) {
        try {
            String url = String.format("%s/?apikey=%s&t=%s",
                    omdbApiProperties.getUrl(), secretsManagerService.getSecret("omdbApiKey"), title);
            return restTemplate.getForEntity(url, OmdbResponse.class).getBody();
        } catch (Exception e) {
            log.error("Unable to get information about the movie with title '{}' due to the following exception: {}", title, e.getMessage());
            return MOVIE_INFO_EMPTY;
        }
    }

    private static final OmdbResponse MOVIE_INFO_EMPTY = new OmdbResponse("False");
}
