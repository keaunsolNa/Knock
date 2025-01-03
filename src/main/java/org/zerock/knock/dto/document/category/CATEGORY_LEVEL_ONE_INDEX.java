package org.zerock.knock.dto.document.category;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;
import org.zerock.knock.dto.document.user.USER_INDEX;

import javax.annotation.Nullable;
import java.util.Set;

@Getter
@Setter
@ToString
@Document(indexName = "category-level-one-index")
public class CATEGORY_LEVEL_ONE_INDEX {

    @Id
    private String categoryLevelOneId;
    private String categoryLevelOneNm;
    @OneToMany
    @Nullable
    private Set<CATEGORY_LEVEL_TWO_INDEX> childCategory;
    @ManyToMany
    private Set<USER_INDEX> favoriteUsers;



}
