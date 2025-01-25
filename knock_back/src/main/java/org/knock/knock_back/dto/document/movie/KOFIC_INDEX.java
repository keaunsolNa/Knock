package org.knock.knock_back.dto.document.movie;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.knock.knock_back.dto.Enum.CategoryLevelOne;
import org.knock.knock_back.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;

@Getter
@Setter
@ToString
@Document(indexName = "kofic-index")
public class KOFIC_INDEX {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String movieId;

    private String KOFICCode;

    @Field(type = FieldType.Text, analyzer = "nori", fielddata = true)
    private String movieNm;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Long prdtYear;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Long openingTime;

    private String[] directors;

    private String[] actors;

    private String[] companyNm;

    @Enumerated(EnumType.STRING)
    private CategoryLevelOne categoryLevelOne;

    @OneToMany
    private Iterable<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwo;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Long runningTime;

    @Builder
    public KOFIC_INDEX
            (String KOFICCode, String movieNm,
             Long prdtYear, Long openingTime, String[] directors,
             String[] companyNm, CategoryLevelOne categoryLevelOne,
             Iterable<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwo)
    {
        this.KOFICCode = KOFICCode;
        this.movieNm = movieNm;
        this.prdtYear = prdtYear;
        this.openingTime = openingTime;
        this.directors = directors;
        this.companyNm = companyNm;
        this.categoryLevelOne = categoryLevelOne;
        this.categoryLevelTwo = categoryLevelTwo;
    }

    public KOFIC_INDEX update
            (String movieNm, Long prdtYear, Long openingTime, String[] directors,
             String[] actors, String[] companyNm, CategoryLevelOne categoryLevelOne,
             Iterable<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwo, Long runningTime)
    {
        this.movieNm = movieNm;
        this.prdtYear = prdtYear;
        this.openingTime = openingTime;
        this.directors = directors;
        this.actors = actors;
        this.companyNm = companyNm;
        this.categoryLevelOne = categoryLevelOne;
        this.categoryLevelTwo = categoryLevelTwo;
        this.runningTime = runningTime;

        return this;
    }

}
