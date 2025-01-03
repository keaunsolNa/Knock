package org.zerock.knock.dto.document.category;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;
import org.zerock.knock.dto.document.user.USER_INDEX;

import java.util.Set;

@Getter
@Setter
@ToString
@Document(indexName = "category-level-two-index")
public class CATEGORY_LEVEL_TWO_INDEX {

    @Id
    private String categoryLevelTwoId;
    private String categoryLevelTwoNm;
    @ManyToOne
    @NonNull
    private CATEGORY_LEVEL_ONE_INDEX parentCategory;
    @ManyToMany
    private Set<USER_INDEX> favoriteUsers;

}
