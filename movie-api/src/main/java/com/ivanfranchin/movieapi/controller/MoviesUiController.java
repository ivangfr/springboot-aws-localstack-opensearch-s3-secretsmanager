package com.ivanfranchin.movieapi.controller;

import com.ivanfranchin.movieapi.mapper.MovieMapper;
import com.ivanfranchin.movieapi.service.MovieService;
import com.ivanfranchin.movieapi.service.PosterService;
import com.ivanfranchin.movieapi.client.OmdbApiClient;
import com.ivanfranchin.movieapi.client.OmdbResponse;
import com.ivanfranchin.movieapi.model.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MoviesUiController {

    private final MovieService movieService;
    private final PosterService posterService;
    private final MovieMapper movieMapper;
    private final OmdbApiClient omdbApiClient;

    @GetMapping("/")
    public String getHome() {
        return "redirect:/movies";
    }

    @GetMapping("/movies")
    public String getMovies(Model model) {
        model.addAttribute("searchRequest", new SearchRequest());
        model.addAttribute("addOmdbResponse", new OmdbResponse());
        model.addAttribute("movies", movieService.getMovies());
        return "movies";
    }

    @GetMapping("/movies/{imdb}")
    public String getMovie(@PathVariable String imdb, Model model) {
        Optional<Movie> movieOptional = movieService.getMovie(imdb);
        if (movieOptional.isEmpty()) {
            return "redirect:/movies";
        }
        model.addAttribute("movie", movieOptional.get());
        return "movieDetail";
    }

    @PostMapping("/movies/search")
    public String searchMovies(@ModelAttribute SearchRequest searchRequest,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(searchRequest.getText())) {
            return "redirect:/movies";
        }
        OmdbResponse omdbResponse;
        try {
            omdbResponse = omdbApiClient.getMovieByTitle(searchRequest.getText());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    String.format("An error occurred while searching for title containing '%s' in OMDb API! Error message: %s",
                            searchRequest.getText(), e.getMessage()));
            return "redirect:/movies";
        }
        if ("False".equals(omdbResponse.getResponse())) {
            redirectAttributes.addFlashAttribute("error",
                    String.format("No movies with title containing '%s' were found!", searchRequest.getText()));
            return "redirect:/movies";
        }
        model.addAttribute("omdbResponse", omdbResponse);
        model.addAttribute("addOmdbResponse", omdbResponse);
        return "movies";
    }

    @PutMapping("/movies")
    public String putMovie(@RequestParam MultipartFile posterFile,
                           @ModelAttribute Movie movie,
                           RedirectAttributes redirectAttributes) {
        try {
            if (!posterFile.isEmpty()) {
                movie.setPoster(posterService.uploadFile(posterFile));
            }
            movieService.saveMovie(movie);
            redirectAttributes.addFlashAttribute("message",
                    String.format("Movie '%s' updated successfully! Refresh the page in case it's not showing the updated version.", movie.getTitle()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    String.format("An error occurred while updating movie '%s'!", movie.getTitle()));
        }
        return "redirect:/movies";
    }

    @PostMapping("/movies")
    public String postMovie(@ModelAttribute OmdbResponse addOmdbResponse,
                            @RequestParam String action,
                            RedirectAttributes redirectAttributes) {
        if ("cancel".equals(action)) {
            return "redirect:/movies";
        }
        Movie movie = movieMapper.toMovie(addOmdbResponse);
        try {
            movieService.saveMovie(movie);
            redirectAttributes.addFlashAttribute("message",
                    String.format("Movie '%s' added successfully! Refresh the page in case it's not showing.", addOmdbResponse.getTitle()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    String.format("An error occurred while adding movie '%s'!", addOmdbResponse.getTitle()));
        }
        return "redirect:/movies";
    }
}
