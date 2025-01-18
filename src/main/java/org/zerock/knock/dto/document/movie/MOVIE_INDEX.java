package org.zerock.knock.dto.document.movie;

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
import org.zerock.knock.dto.Enum.CategoryLevelOne;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@ToString
@Document(indexName = "movie-index")
public class MOVIE_INDEX {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String movieId;

    @Field(type = FieldType.Text, analyzer = "nori", fielddata = true)
    private String movieNm;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Long openingTime;

    private String KOFICCode;

    private String[] reservationLink;

    private String posterBase64;

    private String[] directors;

    private String[] actors;

    private String[] companyNm;

    @Enumerated(EnumType.STRING)
    private CategoryLevelOne categoryLevelOne;

    @OneToMany
    private Iterable<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwo;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Long runningTime;

    private String plot;

    private Iterable<String> favorites;

    @Builder
    public MOVIE_INDEX
            (String movieId, String movieNm, Long openingTime, String KOFICCode,
             String[] reservationLink, String posterBase64, String[] directors,
             String[] actors, String[] companyNm,  CategoryLevelOne categoryLevelOne,
             Iterable<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwo, Long runningTime,
             String plot, Iterable<String> favorites )
    {
        this.movieId = movieId;
        this.movieNm = movieNm;
        this.openingTime = openingTime;
        this.KOFICCode = KOFICCode;
        this.reservationLink = reservationLink;
        this.posterBase64 = posterBase64;
        this.directors = directors;
        this.actors = actors;
        this.companyNm = companyNm;
        this.categoryLevelOne = categoryLevelOne;
        this.categoryLevelTwo = categoryLevelTwo;
        this.runningTime = runningTime;
        this.plot = plot;
        this.favorites = favorites;
    }

    public MOVIE_INDEX update
            (String movieNm, Long openingTime, String[] reservationLink, String posterBase64,
             String[] directors, String[] actors, String[] companyNm,  CategoryLevelOne categoryLevelOne,
             Iterable<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwo, Long runningTime,
             String plot, Iterable<String> favorites )
    {
        this.movieNm = movieNm;
        this.openingTime = openingTime;
        this.reservationLink = reservationLink;
        this.posterBase64 = posterBase64;
        this.directors = directors;
        this.actors = actors;
        this.companyNm = companyNm;
        this.categoryLevelOne = categoryLevelOne;
        this.categoryLevelTwo = categoryLevelTwo;
        this.runningTime = runningTime;
        this.plot = plot;
        this.favorites = favorites;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MOVIE_INDEX movieINDEX)) return false;
        return Objects.equals(KOFICCode, movieINDEX.KOFICCode) || Objects.equals(movieId, movieINDEX.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, movieNm, openingTime, KOFICCode, Arrays.hashCode(reservationLink), posterBase64, Arrays.hashCode(directors), Arrays.hashCode(actors), Arrays.hashCode(companyNm), categoryLevelOne, categoryLevelTwo, runningTime, plot, favorites);
    }
}
