package com.mycompany.movieapi.rest;

import com.mycompany.movieapi.mapper.MovieMapper;
import com.mycompany.movieapi.service.MovieService;
import com.mycompany.movieapi.service.PosterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final PosterService posterService;
    private final MovieMapper movieMapper;

    @GetMapping
    public SearchMovieResponse searchMovie(@RequestParam(required = false) String title) {
        return movieService.searchMovies(title);
    }

    @PostMapping
    public String addMovie(@Valid @RequestBody AddMovieRequest addMovieRequest) {
        return movieService.saveMovie(movieMapper.toMovieMap(addMovieRequest));
    }

    @PostMapping(value = "/{imdb}/uploadPoster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadPoster(@PathVariable String imdb, @RequestParam("poster") MultipartFile poster) {
        Map<String, Object> movieMap = movieService.validateAndGetMovie(imdb);
        String uploadedFile = posterService.uploadFile(poster);
        movieMap.put("poster", uploadedFile);
        movieService.saveMovie(movieMap);
        return uploadedFile;
    }
}
