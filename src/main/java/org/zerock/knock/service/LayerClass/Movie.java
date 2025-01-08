package org.zerock.knock.service.LayerClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.zerock.knock.component.util.MovieDtoToIndex;
import org.zerock.knock.dto.document.movie.KOFIC_INDEX;
import org.zerock.knock.dto.document.movie.MOVIE_INDEX;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;
import org.zerock.knock.repository.movie.MovieRepository;
import org.zerock.knock.service.layerInterface.MovieInterface;

import java.util.Objects;
import java.util.Set;

@Service
public class Movie implements MovieInterface {

    private static final Logger logger = LoggerFactory.getLogger(MovieInterface.class);
    private final MovieMaker movieMaker;
    private final MovieDtoToIndex translation;

    public Movie(MovieRepository movieRepository,
                 ElasticsearchOperations elasticsearchOperations,
                 MovieDtoToIndex translation) {
        this.movieMaker = new MovieMaker(movieRepository, elasticsearchOperations);
        this.translation = translation;
    }

    public Iterable<MOVIE_INDEX> createMovie(Set<MOVIE_DTO> movies) {

        logger.info("{} START", getClass().getSimpleName());

        // DELETE ALL DATA BEFORE CREATE
        movieMaker.deleteMovie();

        Iterable<MOVIE_INDEX> movie = movieMaker.CreateMovie(translation.MovieDtoToIndex(movies));

        logger.info("{} END", getClass().getSimpleName());

        return movie;
    }

    public Iterable<MOVIE_INDEX> readMovies() {

        logger.info("{} START", getClass().getSimpleName());

        Iterable<MOVIE_INDEX> movies = movieMaker.readAllMovie();

        logger.info("{} END", getClass().getSimpleName());

        return movies;
    }

    public MOVIE_INDEX readMoviesDetail(String id) {

        logger.info("{} START", getClass().getSimpleName());

        MOVIE_INDEX movies = movieMaker.readMovieById(id);

        logger.info("{} END", getClass().getSimpleName());

        return movies;
    }

    public KOFIC_INDEX similaritySearch (String nm)
    {
        logger.info("{} START", getClass().getSimpleName());

        SearchHits<KOFIC_INDEX> searchHits = movieMaker.searchKOFICByMovieNm(nm);

        logger.info("{} END", getClass().getSimpleName());

        return Objects.requireNonNull(searchHits.stream().findFirst().orElse(null)).getContent();
    }
}
