package org.zerock.knock.dto.dto.movie;

import lombok.Data;
import org.zerock.knock.dto.Enum.CategoryLevelOne;
import org.zerock.knock.dto.dto.category.CATEGORY_LEVEL_TWO_DTO;

import java.util.Set;

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
    private CategoryLevelOne categoryLevelOne;
    private Iterable<CATEGORY_LEVEL_TWO_DTO> categoryLevelTwo;
    private Long runningTime;
    private String plot;
    private Set<String> favorites;
}
