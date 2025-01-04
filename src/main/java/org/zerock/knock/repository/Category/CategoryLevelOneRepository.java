package org.zerock.knock.repository.Category;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_ONE_INDEX;

public interface CategoryLevelOneRepository extends ElasticsearchRepository<CATEGORY_LEVEL_ONE_INDEX, String>
{
    CATEGORY_LEVEL_ONE_INDEX findByNm(String name);
}
