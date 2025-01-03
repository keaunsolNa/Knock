package org.zerock.knock.repository.Category;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_TWO_INDEX;

public interface CategoryLevelTwoRepository extends ElasticsearchRepository<CATEGORY_LEVEL_TWO_INDEX, String>
{
}
