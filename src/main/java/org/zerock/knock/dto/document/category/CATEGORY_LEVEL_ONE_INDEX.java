package org.zerock.knock.dto.document.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;
import org.zerock.knock.dto.document.user.USER_INDEX;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Document(indexName = "category-level-one-index")
public class CATEGORY_LEVEL_ONE_INDEX {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String nm;

    private Set<CATEGORY_LEVEL_TWO_INDEX> childCategory;

    @ManyToMany
    private Set<USER_INDEX> favoriteUsers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CATEGORY_LEVEL_ONE_INDEX that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(nm, that.nm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nm);
    }
}
