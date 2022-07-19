package com.ivanfranchin.movieapi.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    @NotBlank
    private String region;

    @NotBlank
    private String accessKey;

    @NotBlank
    private String secretAccessKey;

    @NotBlank
    private String endpoint;

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
