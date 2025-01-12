package org.zerock.knock.dto.dto.movie;

import lombok.Data;
import org.zerock.knock.dto.dto.category.CATEGORY_LEVEL_TWO_DTO;
import org.zerock.knock.dto.dto.user.USER_DTO;

@Data
public class MOVIE_DTO {

    private String movieId;
    private String movieNm;
    private String openingTime;
    private String KOFICCode;
    private String[] reservationLink;
    private String posterBase64;
    private String[] directors;
    private String[] actors;
    private String[] companyNm;
    private Iterable<CATEGORY_LEVEL_TWO_DTO> categoryLevelTwo;
    private long runningTime;
    private String plot;
    private Iterable<USER_DTO> favorites;
}
