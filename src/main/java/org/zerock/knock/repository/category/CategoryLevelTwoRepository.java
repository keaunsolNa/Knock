package org.zerock.knock.repository.category;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.zerock.knock.dto.Enum.CategoryLevelOne;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryLevelTwoRepository extends ElasticsearchRepository<CATEGORY_LEVEL_TWO_INDEX, String>
{
    Optional<Set<CATEGORY_LEVEL_TWO_INDEX>> findAllByParentNm(CategoryLevelOne parentNm);
}

