package org.zerock.knock.service.LayerClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.zerock.knock.component.util.MovieDtoToIndex;
import org.zerock.knock.dto.document.movie.MOVIE_INDEX;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;
import org.zerock.knock.repository.movie.MovieRepository;
import org.zerock.knock.service.layerInterface.MovieInterface;

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

        logger.info("[{}]", "CREATE_MOVIE START");

        // DELETE ALL DATA BEFORE CREATE
        movieMaker.deleteMovie();

        return movieMaker.CreateMovie(translation.dtoToIndex(movies));
    }

}
