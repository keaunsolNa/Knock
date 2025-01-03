package org.zerock.knock.dto.document.movie;

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

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Document(indexName = "movie-index")
public class MOVIE_INDEX {

    @Id
    private String movieId;

    private String movieNm;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private long openingTime;

    private String KOFICCode;

    private Set<String> reservationLink;

    private String posterBase64;

    private String director;

    private Set<String> actors;

    private CATEGORY_LEVEL_ONE_INDEX categoryLevelOne;

    @OneToMany
    private CATEGORY_LEVEL_TWO_INDEX categoryLevelTwo;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private long runningTime;

    private String plot;

    private Set<USER_INDEX> favorites;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MOVIE_INDEX movieINDEX)) return false;
        return Objects.equals(KOFICCode, movieINDEX.KOFICCode) || Objects.equals(movieId, movieINDEX.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, movieNm, openingTime, KOFICCode, reservationLink, posterBase64, director, actors, categoryLevelOne, categoryLevelTwo, runningTime, plot, favorites);
    }
}
