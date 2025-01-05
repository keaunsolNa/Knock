package org.zerock.knock.controller.movie;

import org.springframework.web.bind.annotation.*;
import org.zerock.knock.service.LayerClass.Movie;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    private final Movie movie;

    public MovieController(Movie movie) {
        this.movie = movie;
    }

    @GetMapping("/")
    public Iterable<Movie> getMovies() {

        return null;
    }

    @GetMapping("/getDetail")
    public Movie getDetail(@RequestParam String movieId) {

        return null;
    }

    @GetMapping("/recommend")
    public Movie getRecommend(@RequestParam String movieId) {

        return null;
    }

    @PostMapping("/sub")
    public String subscribe(@RequestBody String movieId, @RequestParam String user) {

        return null;
    }

    @PostMapping("cancelSub")
    public String subscribeCancel (@RequestBody String movieId, @RequestParam String userId) {

        return null;
    }
}
