package com.ivanfranchin.movieapi.aws;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    @NotBlank
    private String endpoint;

    @NotBlank
    private String region;

    @NotNull
    private OpenSearch opensearch;

    @NotNull
    private S3 s3;

    @Data
    @Valid
    public static class OpenSearch {

        @NotBlank
        private String domain;

        @NotBlank
        private String indexes;
    }

    @Data
    @Valid
    public static class S3 {

        @NotBlank
        private String bucketName;
    }
}
