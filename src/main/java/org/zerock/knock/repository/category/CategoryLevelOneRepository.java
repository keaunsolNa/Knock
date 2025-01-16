package org.zerock.knock.repository.category;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.zerock.knock.dto.document.category.CATEGORY_LEVEL_ONE_INDEX;

import java.util.Optional;

public interface CategoryLevelOneRepository extends ElasticsearchRepository<CATEGORY_LEVEL_ONE_INDEX, String>
{
    Optional<CATEGORY_LEVEL_ONE_INDEX> findByNm(String name);
}
