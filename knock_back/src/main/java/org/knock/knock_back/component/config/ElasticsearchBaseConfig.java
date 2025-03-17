package org.knock.knock_back.component.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author nks
 * @apiNote ElasticSearch 설정
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "org.knock.knock_back.*")
@ComponentScan(basePackages = {"org.knock.knock_back.*"})
public class ElasticsearchBaseConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.id}")
    private String id;

    @Value("${elasticsearch.password}")
    private String password;

    @Override
    public @NonNull ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .withBasicAuth(id, password)
                .withSocketTimeout(20000)
                .withConnectTimeout(20000)
                .build();
    }
}
