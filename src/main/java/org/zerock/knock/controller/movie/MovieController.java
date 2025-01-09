package org.zerock.knock.controller.movie;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.knock.dto.document.movie.KOFIC_INDEX;
import org.zerock.knock.dto.document.movie.MOVIE_INDEX;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;
import org.zerock.knock.service.LayerClass.Movie;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    private final Movie movieService;

    public MovieController(Movie movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public ResponseEntity<Iterable<MOVIE_DTO>> getMovies() {
        return ResponseEntity.ok(movieService.readMovies());
    }

    @GetMapping("/getDetail")
    public ResponseEntity<MOVIE_INDEX> getDetail(@RequestParam String movieId) {
        return ResponseEntity.ok(movieService.readMoviesDetail(movieId));
    }

    @GetMapping("/recommend")
    public ResponseEntity<Iterable<KOFIC_INDEX>> getRecommend(@RequestParam String movieNm) {

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
