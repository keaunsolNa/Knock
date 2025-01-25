package org.zerock.knock.repository.user;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.zerock.knock.dto.document.user.SSO_USER_INDEX;

@Repository
public interface SSOUserRepository extends ElasticsearchRepository<SSO_USER_INDEX, String> {
}
