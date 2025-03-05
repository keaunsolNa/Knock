package org.knock.knock_back.dto.document.performingArts;

import jakarta.persistence.*;
import lombok.*;
import org.knock.knock_back.dto.Enum.CategoryLevelOne;
import org.knock.knock_back.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Getter
@Setter
@ToString
@Document(indexName = "kopis-index")
public class KOPIS_INDEX {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String code;

    @Field(type = FieldType.Text, analyzer = "nori", fielddata = true)
    private String name;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Date from;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Date to;

    private String[] directors;

    private String[] actors;

    private String[] companyNm;

    private String holeNm;

    private String poster;

    private String story;

    private String area;

    private boolean state;

    private String[] dtguidance;

    private String[] relates;

    @Enumerated(EnumType.STRING)
    private CategoryLevelOne categoryLevelOne;

    @Enumerated(EnumType.STRING)
    private CATEGORY_LEVEL_TWO_INDEX categoryLevelTwo;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Date runningTime;

}
