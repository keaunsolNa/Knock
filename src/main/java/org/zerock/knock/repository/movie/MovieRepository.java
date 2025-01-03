package org.zerock.knock.repository.movie;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.zerock.knock.dto.document.movie.MOVIE_INDEX;

public interface MovieRepository extends ElasticsearchRepository<MOVIE_INDEX, String> {
}
