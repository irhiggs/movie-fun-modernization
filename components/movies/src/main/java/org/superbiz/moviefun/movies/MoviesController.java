package org.superbiz.moviefun.movies;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    private MoviesRepository moviesRepository;

    public MoviesController(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @PostMapping
    public void addMovie(@RequestBody Movie movie) {
        moviesRepository.addMovie(movie);
    }

    @DeleteMapping("/{movieId}")
    public void deleteMovieId(@PathVariable Long movieId) {
        moviesRepository.deleteMovieId(movieId);
    }

    @GetMapping("/count")
    public int count(
        @RequestParam(name = "field", required = false) String field,
        @RequestParam(name = "key", required = false) String key
    ) {
        if (field != null && key != null) {
            return moviesRepository.count(field, key);
        } else {
            return moviesRepository.countAll();
        }
    }
}