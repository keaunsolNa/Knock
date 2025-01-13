package org.zerock.knock.repository.movie;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.zerock.knock.dto.document.movie.MOVIE_INDEX;

import java.util.Optional;

public interface MovieRepository extends ElasticsearchRepository<MOVIE_INDEX, String> {

    Optional<MOVIE_INDEX> findByMovieNm(String movieNm);

}
