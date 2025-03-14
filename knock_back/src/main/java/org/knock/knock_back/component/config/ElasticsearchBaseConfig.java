package org.knock.knock_back.component.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.URI;
import java.net.URISyntaxException;

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

    @Override
    public @NonNull ClientConfiguration clientConfiguration() {

        try {
            // Bonsai URL에서 id와 password 추출
            String sanitizedHost = host.startsWith("http") ? host : "https://" + host;
            URI uri = new URI(sanitizedHost);
            String userInfo = uri.getUserInfo();
            String host = uri.getHost();
            int port = uri.getPort();

            ClientConfiguration.TerminalClientConfigurationBuilder builder = ClientConfiguration.builder()
                    .connectedTo( host + ":" + (port == -1 ? 443 : port)) // 기본 포트 443 (HTTPS)
                    .usingSsl()
                    .withSocketTimeout(20000)
                    .withConnectTimeout(20000)
                    ;

            if (userInfo != null && userInfo.contains(":")) {
                String[] credentials = userInfo.split(":");
                builder.withBasicAuth(credentials[0], credentials[1]);
            }

            return builder.build();

        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid Elasticsearch URI: " + host, e);
        }

    }
}
