package org.zerock.knock.dto.dto.movie;

import lombok.Data;
import org.zerock.knock.dto.dto.category.CATEGORY_LEVEL_ONE_DTO;
import org.zerock.knock.dto.dto.category.CATEGORY_LEVEL_TWO_DTO;
import org.zerock.knock.dto.dto.user.USER_DTO;

@Data
public class MOVIE_DTO {

    private String movieId;
    private String movieNm;
    private long openingTime;
    private String KOFICCode;
    private Iterable<String> reservationLink;
    private String posterBase64;
    private String director;
    private Iterable<String> actors;
    private CATEGORY_LEVEL_ONE_DTO categoryLevelOne;
    private Iterable<CATEGORY_LEVEL_TWO_DTO> categoryLevelTwo;
    private long runningTime;
    private String plot;
    private Iterable<USER_DTO> favorites;
}
