package com.ivanfranchin.movieapi.aws;

import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@RequiredArgsConstructor
@Configuration
public class OpenSearchConfig {

    private final AwsProperties awsProperties;

    @Bean
    RestHighLevelClient restHighLevelClient() throws URISyntaxException {
        return new RestHighLevelClient(
                RestClient.builder(HttpHost.create(awsProperties.getEndpoint()))
                        .setPathPrefix(String.format("/opensearch/%s/%s",
                                awsProperties.getRegion(), awsProperties.getOpensearch().getDomain())));
    }
}
