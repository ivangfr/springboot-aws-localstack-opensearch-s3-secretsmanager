package com.ivanfranchin.movieapi.aws;

import com.ivanfranchin.movieapi.movie.exception.PosterUploaderException;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PosterService {

    private final S3Template s3Template;
    private final AwsProperties awsProperties;

    public String getPosterNotAvailableUrl() {
        return String.format("%s/%s/%s",
                awsProperties.getEndpoint(), awsProperties.getS3().getBucketName(), NOT_AVAILABLE_POSTER);
    }

    public Optional<String> downloadFile(URL fileUrl, String fileName) {
        try {
            Files.createDirectories(Paths.get(TMP_FOLDER));
            String filePath = String.format("%s/%s.jpg", TMP_FOLDER, fileName);
            try (ReadableByteChannel readableByteChannel = Channels.newChannel(fileUrl.openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                 FileChannel fileChannel = fileOutputStream.getChannel()) {
                fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                return Optional.of(filePath);
            }
        } catch (IOException e) {
            log.error("Unable to download file from URL '{}'. Error message: {}", fileUrl, e.getMessage());
            return Optional.empty();
        }
    }

    public String uploadFile(MultipartFile file) {
        try {
            return uploadFile(file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            String message = String.format("Unable to upload MultipartFile %s", file.getOriginalFilename());
            throw new PosterUploaderException(message, e);
        }
    }

    public String uploadFile(File file) {
        try {
            return uploadFile(file.getName(), new FileInputStream(file));
        } catch (IOException e) {
            String message = String.format("Unable to upload File %s", file.getName());
            throw new PosterUploaderException(message, e);
        }
    }

    private String uploadFile(String key, InputStream inputStream) {
        String bucketName = awsProperties.getS3().getBucketName();
        s3Template.upload(bucketName, key, inputStream);
        String s3FileUrl = String.format("%s/%s/%s", awsProperties.getEndpoint(), bucketName, key);
        log.info("File '{}' uploaded successfully in S3! URL: {}", key, s3FileUrl);
        return s3FileUrl;
    }

    private static final String TMP_FOLDER = "tmp/posters";
    public static final String NOT_AVAILABLE_POSTER = "not-available.jpg";
    public static final File NOT_AVAILABLE_POSTER_FILE;

    static {
        try {
            NOT_AVAILABLE_POSTER_FILE = ResourceUtils.getFile("classpath:images/" + NOT_AVAILABLE_POSTER);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
