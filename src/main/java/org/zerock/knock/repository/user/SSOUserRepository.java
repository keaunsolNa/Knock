package org.zerock.knock.repository.user;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.zerock.knock.dto.document.user.SSO_USER_INDEX;

public interface SSOUserRepository extends ElasticsearchRepository<SSO_USER_INDEX, String> {
}
