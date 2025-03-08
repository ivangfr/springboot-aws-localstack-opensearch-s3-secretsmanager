package com.ivanfranchin.movieapi.omdb;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "omdbapi")
public class OmdbApiProperties {

    @NotBlank
    private String url;

    @NotBlank
    private String apiKey;
}
