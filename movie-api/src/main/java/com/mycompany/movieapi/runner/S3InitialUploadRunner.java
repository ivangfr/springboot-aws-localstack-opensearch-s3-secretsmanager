package com.mycompany.movieapi.runner;

import com.mycompany.movieapi.service.PosterService;
import com.mycompany.movieapi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.List;

import static com.mycompany.movieapi.service.PosterServiceImpl.NOT_AVAILABLE_POSTER;

@RequiredArgsConstructor
@Component
public class S3InitialUploadRunner implements CommandLineRunner {

    private final S3Service s3Service;
    private final PosterService posterService;

    @Override
    public void run(String... args) throws FileNotFoundException {
        for (String fileName : filesNames) {
            if (!s3Service.isFilePresent(fileName)) {
                posterService.uploadFile(ResourceUtils.getFile(
                        String.format("classpath:%s/%s", IMAGES_FOLDER, fileName)));
            }
        }
    }

    private static final String IMAGES_FOLDER = "images";
    private static final List<String> filesNames = List.of(NOT_AVAILABLE_POSTER);
}