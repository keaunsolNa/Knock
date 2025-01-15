package org.zerock.knock.service.LayerClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.zerock.knock.component.util.ConvertDTOAndIndex;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;
import org.zerock.knock.dto.document.movie.KOFIC_INDEX;
import org.zerock.knock.dto.document.movie.MOVIE_INDEX;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;
import org.zerock.knock.repository.movie.MovieRepository;
import org.zerock.knock.service.layerInterface.MovieInterface;

import java.util.*;

@Service
public class Movie implements MovieInterface {

    private static final Logger logger = LoggerFactory.getLogger(MovieInterface.class);
    private final MovieMaker movieMaker;
    private final ConvertDTOAndIndex translation;

    public Movie(MovieRepository movieRepository,
                 ElasticsearchOperations elasticsearchOperations,
                 ConvertDTOAndIndex translation) {
        this.movieMaker = new MovieMaker(movieRepository, elasticsearchOperations);
        this.translation = translation;
    }

    public void createMovie(Set<MOVIE_DTO> movies) {

        logger.info("{} START", getClass().getSimpleName());

        // DELETE ALL DATA BEFORE CREATE
        movieMaker.deleteMovie();

        movieMaker.CreateMovie(translation.MovieDtoToIndex(movies));

        logger.info("{} END", getClass().getSimpleName());

    }

    public Iterable<MOVIE_DTO> readMovies() {

        logger.info("{} START", getClass().getSimpleName());

        Iterable<MOVIE_INDEX> movies = movieMaker.readAllMovie();
        Set<MOVIE_DTO> returnValue = translation.MovieIndexToDTO(movies);

        logger.info("{} END", getClass().getSimpleName());

        return returnValue;
    }

    public Optional<MOVIE_INDEX> checkMovie(String movieNm) { return movieMaker.readMovieByNm(movieNm); }

    public MOVIE_DTO readMoviesDetail(String id) {

        logger.info("{} START", getClass().getSimpleName());

        MOVIE_INDEX movies = movieMaker.readMovieById(id);
        MOVIE_DTO returnValue = translation.MovieIndexToDTO(movies);

        logger.info("{} END", getClass().getSimpleName());

        return returnValue;
    }

    public KOFIC_INDEX similaritySearch (String nm)
    {
        logger.info("{} START", getClass().getSimpleName());

        SearchHits<KOFIC_INDEX> searchHits = movieMaker.searchKOFICByMovieNm(nm);

        logger.info("{} END", getClass().getSimpleName());

        return Objects.requireNonNull(searchHits.stream().findFirst().orElse(null)).getContent();
    }

    public Map<String, Object> getCategory()
    {
        logger.info("{} START", getClass().getSimpleName());

        Map<String, Map<String, Object>> categoryMap = new HashMap<>();

        Iterable<MOVIE_INDEX> iter = movieMaker.readAllMovie();

        for (MOVIE_INDEX movie : iter)
        {
            for (CATEGORY_LEVEL_TWO_INDEX category : movie.getCategoryLevelTwo())
            {

                if (categoryMap.containsKey(category.getNm()))
                {
                    Map<String, Object> innerMap = categoryMap.get(category.getNm());

                    @SuppressWarnings("unchecked")
                    List<String> movies = (List<String>) innerMap.get("movies");
                    movies.add(movie.getMovieId());

                }
                else
                {
                    List<String> movies = new ArrayList<>();
                    movies.add(movie.getMovieId());

                    Map<String, Object> innerMap = new HashMap<>();
                    innerMap.put("categoryId", category.getId());
                    innerMap.put("categoryNm", category.getNm());
                    innerMap.put("movies", movies);

                    categoryMap.put(category.getNm(), innerMap);
                }
            }
        }

        List<Map<String, Object>> dataList = new ArrayList<>(categoryMap.values());

        Map<String, Object> result = new HashMap<>();
        result.put("data", dataList);

        logger.info("{} END", getClass().getSimpleName());

        return result;
    }
}
