package org.knock.knock_back.repository.performingArts;

import org.knock.knock_back.dto.document.performingArts.KOPIS_INDEX;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KOPISRepository extends ElasticsearchRepository<KOPIS_INDEX, String>
{
    KOPIS_INDEX findByCode(String code);
}
