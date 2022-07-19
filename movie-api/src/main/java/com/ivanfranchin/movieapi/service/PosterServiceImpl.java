package com.ivanfranchin.movieapi.service;

import com.ivanfranchin.movieapi.property.AwsProperties;
import com.ivanfranchin.movieapi.exception.PosterUploaderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PosterServiceImpl implements PosterService {

    private final S3Client s3Client;
    private final AwsProperties awsProperties;

    @Override
    public String getPosterNotAvailableUrl() {
        return String.format("%s/%s/%s",
                awsProperties.getEndpoint(), awsProperties.getS3().getBucketName(), NOT_AVAILABLE_POSTER);
    }

    @Override
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

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            return uploadFile(file.getOriginalFilename(), file.getBytes());
        } catch (IOException e) {
            String message = String.format("Unable to upload MultipartFile %s", file.getOriginalFilename());
            throw new PosterUploaderException(message, e);
        }
    }

    @Override
    public String uploadFile(File file) {
        try {
            return uploadFile(file.getName(), Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            String message = String.format("Unable to upload File %s", file.getName());
            throw new PosterUploaderException(message, e);
        }
    }

    private String uploadFile(String keyName, byte[] bytes) {
        String bucketName = awsProperties.getS3().getBucketName();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(ByteBuffer.wrap(bytes)));

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        String s3FileUrl = s3Client.utilities().getUrl(getUrlRequest).toString();
        log.info("File '{}' uploaded successfully in S3! URL: {}", keyName, s3FileUrl);
        return s3FileUrl;
    }

    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.hasText(fileName)) {
            int idx = fileName.lastIndexOf('.');
            if (idx > 0) {
                String extension = fileName.substring(idx);
                if (StringUtils.hasText(extension)) {
                    return extension;
                }
            }
        }
        return "";
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
