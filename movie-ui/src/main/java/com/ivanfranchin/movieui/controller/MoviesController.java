package com.ivanfranchin.movieui.controller;

import com.ivanfranchin.movieui.model.Movie;
import com.ivanfranchin.movieui.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MoviesController {

    private final MovieService movieService;

    @GetMapping("/")
    public String getHome() {
        return "redirect:/movies";
    }

    @GetMapping("/movies")
    public String getMovies(Model model) {
        model.addAttribute("searchRequest", new SearchRequest());
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
        List<Movie> movies = movieService.searchMovies(searchRequest.getText());
        if (movies.size() == 0) {
            redirectAttributes.addFlashAttribute("message",
                    String.format("No movies with title containing '%s' were found!", searchRequest.getText()));
            return "redirect:/movies";
        }
        model.addAttribute("movies", movies);
        return "movies";
    }
}
