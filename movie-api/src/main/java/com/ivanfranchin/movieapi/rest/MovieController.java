package com.ivanfranchin.movieapi.rest;

import com.ivanfranchin.movieapi.mapper.MovieMapper;
import com.ivanfranchin.movieapi.rest.dto.AddMovieRequest;
import com.ivanfranchin.movieapi.rest.dto.MovieResponse;
import com.ivanfranchin.movieapi.rest.dto.SearchResponse;
import com.ivanfranchin.movieapi.service.MovieService;
import com.ivanfranchin.movieapi.service.PosterService;
import com.ivanfranchin.movieapi.model.Movie;
import lombok.RequiredArgsConstructor;
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

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final PosterService posterService;
    private final MovieMapper movieMapper;

    @GetMapping
    public SearchResponse searchMovie(@RequestParam(required = false) String title) {
        return movieService.searchMovies(title);
    }

    @GetMapping("/{imdb}")
    public MovieResponse getMovie(@PathVariable String imdb) {
        Movie movie = movieService.validateAndGetMovie(imdb);
        return movieMapper.toMovieResponse(movie);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MovieResponse addMovie(@Valid @RequestBody AddMovieRequest addMovieRequest) {
        Movie movie = movieService.saveMovie(movieMapper.toMovie(addMovieRequest));
        return movieMapper.toMovieResponse(movie);
    }

    @PostMapping(value = "/{imdb}/uploadPoster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadPoster(@PathVariable String imdb, @RequestParam("poster") MultipartFile poster) {
        Movie movie = movieService.validateAndGetMovie(imdb);
        String uploadedFile = posterService.uploadFile(poster);
        movie.setPoster(uploadedFile);
        movieService.saveMovie(movie);
        return uploadedFile;
    }
}
