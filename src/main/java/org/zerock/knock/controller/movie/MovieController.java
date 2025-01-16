package org.zerock.knock.controller.movie;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.knock.dto.document.movie.KOFIC_INDEX;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;
import org.zerock.knock.service.layerClass.Movie;

import java.util.Map;

/**
 * @author nks
 * @apiNote Movie 요청을 받는 Controller
 */
@RestController
@RequestMapping("/api/movie")
public class MovieController {

    private final Movie movieService;
    public MovieController(Movie movieService) {
        this.movieService = movieService;
    }

    /**
     * 요청 시 MOVIE 인덱스에 저장된 모든 객체 정보를 가져와 반환한다. 
     * 반환 시 openingTime, poster 정보는 변환하여 반환
     * @return ResponseEntity<Iterable<MOVIE_DTO>> : Movie 저장된 모든 영화 정보
     */
    @GetMapping()
    public ResponseEntity<Iterable<MOVIE_DTO>> getMovies() {
        return ResponseEntity.ok(movieService.readMovies());
    }

    /**
     * 요청 시 MOVIE 인덱스에 대상 객체 정보를 가져와 반환한다.
     * 반환 시 openingTime, poster 정보는 변환하여 반환
     * @param movieId : 대상 영화의 ID
     * @return ResponseEntity<Iterable<MOVIE_DTO>> : Movie 저장된 모든 영화 정보
     */
    @GetMapping("/getDetail")
    public ResponseEntity<MOVIE_DTO> getDetail(@RequestParam String movieId) {
        return ResponseEntity.ok(movieService.readMoviesDetail(movieId));
    }

    /**
     * 요청 시 현재 상영 예정작 영화에 있는 모든 LEVEL_TWO CATEGORY 정보를 가져와 반환
     * @return ResponseEntity<Iterable<CATEGORY_LEVEL_TWO_DTO>> : 현재 상영 예정 리스트에 있는 영화들
     */
    @GetMapping("/getCategory")
    public ResponseEntity<Map<String, Object>> getCategory() {

        return ResponseEntity.ok(movieService.getCategory());
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
