package com.mycompany.movieapi.controller;

import com.mycompany.movieapi.client.OmdbApiClient;
import com.mycompany.movieapi.client.OmdbResponse;
import com.mycompany.movieapi.mapper.MovieMapper;
import com.mycompany.movieapi.model.Movie;
import com.mycompany.movieapi.rest.SearchMovieResponse;
import com.mycompany.movieapi.service.MovieService;
import com.mycompany.movieapi.service.PosterService;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MoviesUiController {

    private final MovieService movieService;
    private final PosterService posterService;
    private final MovieMapper movieMapper;
    private final OmdbApiClient omdbApiClient;

    @GetMapping("/")
    public String getUserHome() {
        return "redirect:/user/movies";
    }

    @GetMapping("/user/movies")
    public String getUserMovies(Model model) {
        model.addAttribute("searchRequest", new SearchRequest());
        model.addAttribute("movies", getMovieList(movieService.searchMovies(null)));
        return "userMovies";
    }

    @GetMapping("/user/movies/{imdb}")
    public String getUserMovie(@PathVariable String imdb, Model model) {
        Optional<Map<String, Object>> sourceOptional = movieService.getMovie(imdb);
        if (sourceOptional.isEmpty()) {
            return "redirect:/user/movies";
        }
        model.addAttribute("movie", movieMapper.toMovie(sourceOptional.get()));
        return "userMovieDetail";
    }

    @PostMapping("/user/movies/search")
    public String searchUserMovies(@ModelAttribute SearchRequest searchRequest,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(searchRequest.getText())) {
            return "redirect:/user/movies";
        }
        SearchMovieResponse searchMovieResponse = movieService.searchMovies(searchRequest.getText());
        if (searchMovieResponse.getHits().size() == 0) {
            redirectAttributes.addFlashAttribute("message",
                    String.format("No movie with title containing '%s' found!", searchRequest.getText()));
            return "redirect:/user/movies";
        }
        model.addAttribute("movies", getMovieList(searchMovieResponse));
        return "userMovies";
    }

    @GetMapping("/admin")
    public String getAdminHome() {
        return "redirect:/admin/movies";
    }

    @GetMapping("/admin/movies")
    public String getAdminMovies(Model model) {
        model.addAttribute("searchRequest", new SearchRequest());
        model.addAttribute("addOmdbResponse", new OmdbResponse());
        model.addAttribute("movies", getMovieList(movieService.searchMovies(null)));
        return "adminMovies";
    }

    @GetMapping("/admin/movies/{imdb}")
    public String getAdminMovie(@PathVariable String imdb, Model model) {
        Optional<Map<String, Object>> sourceOptional = movieService.getMovie(imdb);
        if (sourceOptional.isEmpty()) {
            return "redirect:/admin/movies";
        }
        model.addAttribute("movie", movieMapper.toMovie(sourceOptional.get()));
        return "adminMovieDetail";
    }

    @PostMapping("/admin/movies/search")
    public String searchAdminMovies(@ModelAttribute SearchRequest searchRequest,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(searchRequest.getText())) {
            return "redirect:/admin/movies";
        }
        OmdbResponse omdbResponse = omdbApiClient.getMovieByTitle(searchRequest.getText());
        if ("False".equals(omdbResponse.getResponse())) {
            redirectAttributes.addFlashAttribute("error",
                    String.format("No movie with title containing '%s' found!", searchRequest.getText()));
            return "redirect:/admin/movies";
        }
        model.addAttribute("omdbResponse", omdbResponse);
        model.addAttribute("addOmdbResponse", omdbResponse);
        return "adminMovies";
    }

    @PutMapping("/admin/movies")
    public String putAdminMovie(@ModelAttribute("movie") Movie movie,
                                RedirectAttributes redirectAttributes) {
        Map<String, Object> source = movieMapper.toSource(movie);
        try {
            movieService.saveMovie(source);
            redirectAttributes.addFlashAttribute("message",
                    String.format("Movie '%s' updated successfully! Refresh the page in case it's not showing the updated version.", movie.getTitle()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    String.format("An error occurred while updating movie '%s'!", movie.getTitle()));
        }
        return "redirect:/admin/movies";
    }

    @PostMapping("/admin/movies")
    public String postAdminMovie(@ModelAttribute OmdbResponse addOmdbResponse,
                                 @RequestParam String action,
                                 RedirectAttributes redirectAttributes) {
        if ("cancel".equals(action)) {
            return "redirect:/admin/movies";
        }
        Map<String, Object> source = movieMapper.toSource(addOmdbResponse);
        try {
            movieService.saveMovie(source);
            redirectAttributes.addFlashAttribute("message",
                    String.format("Movie '%s' added successfully! Refresh the page in case it's not showing.", addOmdbResponse.getTitle()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    String.format("An error occurred while adding movie '%s'!", addOmdbResponse.getTitle()));
        }
        return "redirect:/admin/movies";
    }

    @PostMapping("/admin/movies/{imdb}/uploadPoster")
    public String uploadAdminMoviePoster(@RequestParam MultipartFile file,
                                         @PathVariable String imdb,
                                         RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            return "redirect:/admin/movies";
        }
        Optional<Map<String, Object>> sourceOptional = movieService.getMovie(imdb);
        if (sourceOptional.isEmpty()) {
            return "redirect:/admin/movies";
        }
        Map<String, Object> source = sourceOptional.get();
        String title = String.valueOf(source.get("title"));
        try {
            String uploadFileUrl = posterService.uploadFile(file, String.valueOf(source.get("imdb")));
            source.put("poster", uploadFileUrl);
            movieService.saveMovie(source);
            redirectAttributes.addFlashAttribute("message",
                    String.format("The poster of the movie '%s' was uploaded successfully!", title));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    String.format("An error occurred while uploading the poster of the movie '%s'!", title));
        }
        return "redirect:/admin/movies";
    }

    private List<Movie> getMovieList(SearchMovieResponse searchMovieResponse) {
        if (searchMovieResponse.getHits() == null) {
            return Collections.emptyList();
        }
        return searchMovieResponse.getHits()
                .stream()
                .map(SearchMovieResponse.Hit::getSource)
                .map(movieMapper::toMovie)
                .collect(Collectors.toList());
    }
}
