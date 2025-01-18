package org.zerock.knock.dto.document.category;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.zerock.knock.dto.Enum.CategoryLevelOne;

import java.util.Objects;

@Getter
@Setter
@ToString
@Document(indexName = "category-level-two-index")
public class CATEGORY_LEVEL_TWO_INDEX {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori", fielddata = true)
    @Column(nullable = false)
    private String nm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Field(type = FieldType.Text, analyzer = "nori", fielddata = true)
    private CategoryLevelOne parentNm;

    @ManyToMany
    private Iterable<String> favoriteUsers;

    @Builder
    @PersistenceCreator
    public CATEGORY_LEVEL_TWO_INDEX(String id, String nm, CategoryLevelOne parentNm, Iterable<String> favoriteUsers)
    {
        this.id = id;
        this.nm = nm;
        this.parentNm = parentNm;
        this.favoriteUsers = favoriteUsers;
    }

    @Builder
    public CATEGORY_LEVEL_TWO_INDEX(String nm, CategoryLevelOne parentNm)
    {
        this.nm = nm;
        this.parentNm = parentNm;
    }

    public CATEGORY_LEVEL_TWO_INDEX update(String id, String nm, CategoryLevelOne parentNm, Iterable<String> favoriteUsers)
    {
        this.id = id;
        this.nm = nm;
        this.parentNm = parentNm;
        this.favoriteUsers = favoriteUsers;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CATEGORY_LEVEL_TWO_INDEX that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(nm, that.nm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nm);
    }
}
