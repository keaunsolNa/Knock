package org.zerock.knock.repository.movie;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.zerock.knock.dto.document.movie.KOFIC_INDEX;

@Repository
public interface KOFICRepository extends ElasticsearchRepository<KOFIC_INDEX, String>
{
    KOFIC_INDEX findByKOFICCode(String cd);
}
