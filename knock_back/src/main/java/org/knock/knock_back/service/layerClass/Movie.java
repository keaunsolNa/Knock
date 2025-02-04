package org.knock.knock_back.service.layerClass;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.knock.knock_back.component.util.converter.ConvertDTOAndIndex;
import org.knock.knock_back.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;
import org.knock.knock_back.dto.document.movie.KOFIC_INDEX;
import org.knock.knock_back.dto.document.movie.MOVIE_INDEX;
import org.knock.knock_back.dto.document.user.SSO_USER_INDEX;
import org.knock.knock_back.dto.dto.movie.MOVIE_DTO;
import org.knock.knock_back.repository.movie.MovieRepository;
import org.knock.knock_back.repository.user.SSOUserRepository;
import org.knock.knock_back.service.layerInterface.MovieInterface;

import java.util.*;

@Service
public class Movie implements MovieInterface {

    private final MovieMaker movieMaker;
    private final ConvertDTOAndIndex translation;
    private final SSOUserRepository ssoUserRepository;

    public Movie(MovieRepository movieRepository,
                 ElasticsearchOperations elasticsearchOperations,
                 ConvertDTOAndIndex translation, SSOUserRepository ssoUserRepository) {
        this.ssoUserRepository = ssoUserRepository;
        this.movieMaker = new MovieMaker(movieRepository, elasticsearchOperations);
        this.translation = translation;
    }

    public void createMovie(Set<MOVIE_DTO> movies) {

        // DELETE ALL DATA BEFORE CREATE
        movieMaker.deleteMovie();

        movieMaker.CreateMovie(translation.MovieDtoToIndex(movies));

    }

    public Iterable<MOVIE_DTO> readMovies() {

        Iterable<MOVIE_INDEX> movies = movieMaker.readAllMovie();

        return translation.MovieIndexToDTO(movies);
    }

    public Optional<MOVIE_INDEX> checkMovie(String movieNm) { return movieMaker.readMovieByNm(movieNm); }

    public MOVIE_DTO readMoviesDetail(String id) {

        MOVIE_INDEX movies = movieMaker.readMovieById(id);

        return translation.MovieIndexToDTO(movies);
    }

    public KOFIC_INDEX similaritySearch (String nm)
    {
        SearchHits<KOFIC_INDEX> searchHits = movieMaker.searchKOFICByMovieNm(nm);

        return Objects.requireNonNull(searchHits.stream().findFirst().orElse(null)).getContent();
    }

    public Map<String, Object> getCategory()
    {
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

        return result;
    }

    public boolean subscribeMovie (String userId, String movieId)
    {
        Optional<SSO_USER_INDEX> user = ssoUserRepository.findById(userId);

        if (user.isEmpty()) {
            return false;
        }

        MOVIE_INDEX movie = movieMaker.readMovieById(movieId);
        if (movie.getFavorites() == null) movie.setFavorites(new HashSet<>());
        movie.getFavorites().add(userId);

        movieMaker.updateMovie(movie);

        return true;
    }

    public boolean subscribeCancelMovie (String userId, String movieId)
    {
        Optional<SSO_USER_INDEX> user = ssoUserRepository.findById(userId);

        if (user.isEmpty()) {
            return false;
        }

        MOVIE_INDEX movie = movieMaker.readMovieById(movieId);
        if (movie.getFavorites() == null) movie.setFavorites(new HashSet<>());
        movie.getFavorites().remove(userId);

        movieMaker.updateMovie(movie);

        return true;
    }

    public boolean subscribeCheck(String userId, String movieId)
    {
        Optional<SSO_USER_INDEX> user = ssoUserRepository.findById(userId);

        if (user.isEmpty()) {
            return false;
        }

        MOVIE_INDEX movie = movieMaker.readMovieById(movieId);
        if (movie.getFavorites() == null) movie.setFavorites(new HashSet<>());

        return movie.getMovieId().contains(userId);
    }

}
