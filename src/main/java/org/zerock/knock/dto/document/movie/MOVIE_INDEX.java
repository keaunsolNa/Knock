package org.zerock.knock.dto.document.movie;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_ONE_INDEX;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;
import org.zerock.knock.dto.document.user.USER_INDEX;

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
    private long openingTime;

    private String KOFICCode;

    private String[] reservationLink;

    private String posterBase64;

    private String director;

    private Iterable<String> actors;

    private String companyNm;

    private CATEGORY_LEVEL_ONE_INDEX categoryLevelOne;

    @OneToMany
    private Iterable<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwo;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Long runningTime;

    private String plot;

    private Iterable<USER_INDEX> favorites;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MOVIE_INDEX movieINDEX)) return false;
        return Objects.equals(KOFICCode, movieINDEX.KOFICCode) || Objects.equals(movieId, movieINDEX.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, movieNm, openingTime, KOFICCode, Arrays.hashCode(reservationLink), posterBase64, director, actors, categoryLevelOne, categoryLevelTwo, runningTime, plot, favorites);
    }
}
