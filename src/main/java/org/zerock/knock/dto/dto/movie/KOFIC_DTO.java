package org.zerock.knock.dto.dto.movie;

import lombok.Data;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_ONE_INDEX;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;

@Data
public class KOFIC_DTO {

    private String movieId;

    private String KOFICCode;

    private String movieNm;

    private Long prdtYear;

    private long openingTime;

    private String director;

    private String companyNm;

    private CATEGORY_LEVEL_ONE_INDEX categoryLevelOne;

    private Iterable<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwo;

    private Long runningTime;

}
