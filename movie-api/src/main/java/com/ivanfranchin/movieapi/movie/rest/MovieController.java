package com.ivanfranchin.movieapi.movie.rest;

import com.ivanfranchin.movieapi.aws.PosterService;
import com.ivanfranchin.movieapi.movie.model.Movie;
import com.ivanfranchin.movieapi.movie.MovieService;
import com.ivanfranchin.movieapi.movie.rest.dto.AddMovieRequest;
import com.ivanfranchin.movieapi.movie.rest.dto.MovieResponse;
import com.ivanfranchin.movieapi.movie.rest.dto.SearchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final PosterService posterService;

    @GetMapping
    public SearchResponse searchMovie(@RequestParam(required = false) String title) {
        log.info("Search movie with title {}", title);
        return movieService.searchMovies(title);
    }

    @GetMapping("/{imdb}")
    public MovieResponse getMovie(@PathVariable String imdb) {
        log.info("Search movie with imdb {}", imdb);
        Movie movie = movieService.validateAndGetMovie(imdb);
        return MovieResponse.from(movie);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MovieResponse addMovie(@Valid @RequestBody AddMovieRequest addMovieRequest) {
        log.info("Add movie {}", addMovieRequest);
        Movie movie = movieService.saveMovie(Movie.from(addMovieRequest));
        return MovieResponse.from(movie);
    }

    @PostMapping(value = "/{imdb}/uploadPoster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadPoster(@PathVariable String imdb, @RequestParam("poster") MultipartFile poster) {
        log.info("Upload poster with imdb {}", imdb);
        Movie movie = movieService.validateAndGetMovie(imdb);
        String uploadedFile = posterService.uploadFile(poster);
        movie.setPoster(uploadedFile);
        movieService.saveMovie(movie);
        return uploadedFile;
    }
}
