package com.ivanfranchin.movieapi.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class OmdbApiClientConfig {

    @Value("${omdbapi.url}")
    private String omdbApiUrl;

    @Bean
    OmdbApiClient omdbApiClient(RestClient.Builder builder) {
        RestClient restClient = builder.baseUrl(omdbApiUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(OmdbApiClient.class);
    }
}
