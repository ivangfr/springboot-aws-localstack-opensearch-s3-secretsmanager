package com.ivanfranchin.movieapi.client;

import com.ivanfranchin.movieapi.property.OmdbApiProperties;
import com.ivanfranchin.movieapi.service.SecretsManagerService;
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

    public OmdbResponse getMovieByTitle(String title) {
        String url = String.format("%s/?apikey=%s&t=%s",
                omdbApiProperties.getUrl(), secretsManagerService.getSecret("omdbApiKey"), title);
        return restTemplate.getForEntity(url, OmdbResponse.class).getBody();
    }
}
