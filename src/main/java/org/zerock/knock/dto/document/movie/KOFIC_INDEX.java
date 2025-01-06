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
    private long openingTime;

    private String[] director;

    private String[] companyNm;

    private CATEGORY_LEVEL_ONE_INDEX categoryLevelOne;

    @OneToMany
    private Iterable<CATEGORY_LEVEL_TWO_INDEX> categoryLevelTwo;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Long runningTime;
}
