package com.ivanfranchin.movieapi.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.util.Optional;

public interface PosterService {

    String getPosterNotAvailableUrl();

    Optional<String> downloadFile(URL fileUrl, String fileName);

    String uploadFile(MultipartFile file);

    String uploadFile(File file);
}
