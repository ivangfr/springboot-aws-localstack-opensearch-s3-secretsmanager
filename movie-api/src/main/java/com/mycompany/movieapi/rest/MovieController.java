package com.mycompany.movieapi.rest;

import com.mycompany.movieapi.mapper.MovieMapper;
import com.mycompany.movieapi.service.MovieService;
import com.mycompany.movieapi.service.PosterService;
import lombok.RequiredArgsConstructor;
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
        return movieService.saveMovie(movieMapper.toSource(addMovieRequest));
    }

    @PostMapping(value = "/{imdb}/uploadPoster", consumes = "multipart/form-data")
    public String uploadPoster(@PathVariable String imdb, @RequestParam("poster") MultipartFile poster) {
        Map<String, Object> movie = movieService.validateAndGetMovie(imdb);
        String uploadFile = posterService.uploadFile(poster, imdb);
        movie.put("poster", uploadFile);
        movieService.saveMovie(movie);
        return uploadFile;
    }
}
