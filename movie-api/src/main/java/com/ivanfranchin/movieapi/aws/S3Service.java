package com.ivanfranchin.movieapi.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final S3Client s3Client;
    private final AwsProperties awsProperties;

    public boolean isFilePresent(String fileName) {
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .bucket(awsProperties.getS3().getBucketName())
                .build();

        return s3Client.listObjects(listObjectsRequest)
                .contents()
                .stream()
                .anyMatch(s3Object -> s3Object.key().equals(fileName));
    }
}
