package com.ivanfranchin.movieapi.aws;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class OpenSearchConfig {

    private final AwsProperties awsProperties;

    @Bean
    RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(HttpHost.create(awsProperties.getEndpoint()))
                        .setPathPrefix(String.format("/opensearch/%s/%s",
                                awsProperties.getRegion(), awsProperties.getOpensearch().getDomain())));
    }
}
