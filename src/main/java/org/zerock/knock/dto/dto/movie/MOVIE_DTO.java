package org.zerock.knock.dto.dto.movie;

import lombok.Data;
import org.zerock.knock.dto.dto.category.CATEGORY_LEVEL_ONE_DTO;
import org.zerock.knock.dto.dto.category.CATEGORY_LEVEL_TWO_DTO;
import org.zerock.knock.dto.dto.user.USER_DTO;

import java.util.Set;

@Data
public class MOVIE_DTO {

    private String movieId;
    private String movieNm;
    private long openingTime;
    private String KOFICCode;
    private Set<String> reservationLink;
    private String posterBase64;
    private String director;
    private Set<String> actors;
    private CATEGORY_LEVEL_ONE_DTO categoryLevelOne;
    private Set<CATEGORY_LEVEL_TWO_DTO> categoryLevelTwo;
    private long runningTime;
    private String plot;
    private Set<USER_DTO> favorites;
}
