package com.ivanfranchin.movieui.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class MovieApiClientConfig {

    @Value("${movie-api.url}")
    private String movieApiUrl;

    @Bean
    public MovieApiClient movieApiClient() {
        RestClient restClient = RestClient.builder().baseUrl(movieApiUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(MovieApiClient.class);
    }
}
