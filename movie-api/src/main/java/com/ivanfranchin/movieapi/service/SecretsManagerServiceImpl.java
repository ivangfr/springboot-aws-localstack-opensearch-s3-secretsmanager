package com.ivanfranchin.movieapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

@RequiredArgsConstructor
@Service
public class SecretsManagerServiceImpl implements SecretsManagerService {

    private final SecretsManagerClient secretsManagerClient;

    @Override
    public String getSecret(String secretName) {
        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        return secretsManagerClient.getSecretValue(valueRequest).secretString();
    }
}
