package org.zerock.knock.service.layerInterface;

import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.zerock.knock.dto.document.movie.KOFIC_INDEX;
import org.zerock.knock.dto.document.movie.MOVIE_INDEX;
import org.zerock.knock.repository.movie.MovieRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface MovieInterface {

    class MovieMaker {

        private final MovieRepository movieRepository;
        private final ElasticsearchOperations elasticsearchOperations;
        // Constructor
        public MovieMaker(MovieRepository movieRepository, ElasticsearchOperations elasticsearchOperations) {
            this.movieRepository = movieRepository;
            this.elasticsearchOperations = elasticsearchOperations;
        }

        public void CreateMovie(Set<MOVIE_INDEX> movieINDEX) {
            movieRepository.saveAll(movieINDEX);
        }

        public Iterable<MOVIE_INDEX> readAllMovie() {

            NativeQuery query = NativeQuery.builder()
                    .withQuery(q -> q.matchAll(m -> m
                    ))
                    .withSort(Sort.by(Sort.Order.asc("openingTime")))
                    .withMaxResults(100)
                    .build();

            SearchHits<MOVIE_INDEX> searchHits = elasticsearchOperations.search(query, MOVIE_INDEX.class);

            return searchHits.getSearchHits()
                    .stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList());
        }

        public Optional<MOVIE_INDEX> readMovieByNm(String nm) { return movieRepository.findByMovieNm(nm); }

        public MOVIE_INDEX readMovieById(String id) { return movieRepository.findById(id).orElseThrow(); }

        public SearchHits<KOFIC_INDEX> searchKOFICByMovieNm(String movieNm)
        {
            NativeQuery query = NativeQuery.builder()
                    .withQuery(q -> q.match(m -> m
                            .field("movieNm")
                            .query(movieNm)
                            .fuzziness("AUTO")
                    ))
                    .withSort(Sort.by(Sort.Order.desc("_score")))
                    .withMaxResults(100)
                    .build();

            return elasticsearchOperations.search(query, KOFIC_INDEX.class);
        }

        public Iterable<MOVIE_INDEX> updateMovie(Set<MOVIE_INDEX> movieINDEX) {

            Set<MOVIE_INDEX> updateList = new HashSet<>();
            for (MOVIE_INDEX movie : movieINDEX)
            {
                MOVIE_INDEX movieIndex = movieRepository.findById(movie.getMovieId()).orElseThrow();
                movieIndex.setFavorites(movie.getFavorites());
                movieIndex.setReservationLink(movie.getReservationLink());
                updateList.add(movieIndex);
            }

            return movieRepository.saveAll(updateList);
        }

        public void deleteMovie() {
            movieRepository.deleteAll();
        }

        public void deleteById(String id) {
            movieRepository.deleteById(id);
        }
    }
}
