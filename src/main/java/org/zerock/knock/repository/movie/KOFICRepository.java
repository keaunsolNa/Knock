package org.zerock.knock.repository.movie;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.zerock.knock.dto.document.movie.KOFIC_INDEX;

public interface KOFICRepository extends ElasticsearchRepository<KOFIC_INDEX, String>
{
    KOFIC_INDEX findByKOFICCode(String cd);
}
