package org.zerock.knock.service.layerInterface;

import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.zerock.knock.dto.document.movie.MOVIE_INDEX;
import org.zerock.knock.repository.movie.MovieRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.Set;

public interface MovieInterface {

    String INDEX_SCORE = "_score";

    class MovieMaker {

        private final MovieRepository movieRepository;
        private final ElasticsearchOperations elasticsearchOperations;

        public MovieMaker(MovieRepository movieRepository, ElasticsearchOperations elasticsearchOperations) {
            this.movieRepository = movieRepository;
            this.elasticsearchOperations = elasticsearchOperations;
        }

        public Iterable<MOVIE_INDEX> CreateMovie(Set<MOVIE_INDEX> movieINDEX) {
            return movieRepository.saveAll(movieINDEX);
        }

        public Iterable<MOVIE_INDEX> readAllMovie() {
            return movieRepository.findAll();
        }

        public Iterable<MOVIE_INDEX> readMovie(String openingTime, String categoryId) {

            int year = Integer.parseInt(openingTime.substring(0, 4));
            int month = Integer.parseInt(openingTime.substring(4));
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

            org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder builder = NativeQuery.builder();
            builder.withQuery(q -> q.bool(bool ->
                    bool.must(term -> term.match(wild -> wild.field("categoryLevelTwoId").query(categoryId)))));
            builder.withQuery(q -> q.bool(bool ->
                    bool.must(term -> term.range(wild -> wild.date(st -> st.gt(LocalDateTime.of(year, month, 0, 0, 0).format(formatter)))))));
            builder.withMaxResults(1000);
            builder.withSort(Sort.by(INDEX_SCORE).descending());
            NativeQuery query = builder
                    .build();

            SearchHits<MOVIE_INDEX> searchHits = elasticsearchOperations.search(query, MOVIE_INDEX.class);

            Set<MOVIE_INDEX> resultSet = new HashSet<>();

            for (SearchHit<MOVIE_INDEX> hits : searchHits)
            {
                resultSet.add(hits.getContent());
            }

            return resultSet;
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
