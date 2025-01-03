package org.zerock.knock.component.util;

import org.springframework.stereotype.Component;
import org.zerock.knock.dto.document.movie.MOVIE_INDEX;
import org.zerock.knock.dto.dto.movie.MOVIE_DTO;

import java.util.HashSet;
import java.util.Set;

@Component
public class MovieDtoToIndex {

    public Set<MOVIE_INDEX> dtoToIndex(Set<MOVIE_DTO> dtos)
    {

        Set<MOVIE_INDEX> result = new HashSet<>();
        for (MOVIE_DTO dto : dtos) {

            MOVIE_INDEX index = new MOVIE_INDEX();
            index.setMovieId(dto.getMovieId());
            index.setMovieNm(dto.getMovieNm());
            index.setOpeningTime(dto.getOpeningTime());
            index.setKOFICCode(dto.getKOFICCode());
            index.setReservationLink(dto.getReservationLink());
            index.setPosterBase64(dto.getPosterBase64());
            index.setDirector(dto.getDirector());
            index.setActors(dto.getActors());
            //        index.setCategoryLevelOne(dto.getCategoryLevelOne());
            //        index.setCategoryLevelTwo(dto.getCategoryLevelTwo());
            index.setRunningTime(dto.getRunningTime());
            index.setPlot(dto.getPlot());
            //        index.setFavorites(dto.getFavorites());

            result.add(index);
        }

        return result;
    }
}
