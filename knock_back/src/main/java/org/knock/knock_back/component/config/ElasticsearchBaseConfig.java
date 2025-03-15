package org.knock.knock_back.component.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author nks
 * @apiNote ElasticSearch 설정
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "org.knock.knock_back.*")
@ComponentScan(basePackages = {"org.knock.knock_back.*"})
public class ElasticsearchBaseConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Bean
    public ElasticsearchClient elasticsearchClient() {

        try {
            // Bonsai URL에서 id와 password 추출
            String sanitizedHost = host.startsWith("http") ? host : "https://" + host;
            URI uri = new URI(sanitizedHost);
            String userInfo = uri.getUserInfo();
            String host = uri.getHost();
            int port = uri.getPort() == -1 ? 443 : uri.getPort(); // 기본 포트 설정

            // HTTP 헤더 설정 (X-Elastic-Product 추가)
            List<BasicHeader> headers = new ArrayList<>();
            headers.add(new BasicHeader("Content-Type", "application/json"));
            headers.add(new BasicHeader("X-Elastic-Product", "Elasticsearch")); // 🔥 중요! 이 헤더가 누락되면 Bonsai가 차단

            // 인증 정보가 있다면 Authorization 헤더 추가
            if (userInfo != null && userInfo.contains(":")) {
//                String[] credentials = userInfo.split(":");
//                String auth = Base64.getEncoder().encodeToString((credentials[0] + ":" + credentials[1]).getBytes());
                String credentials = "randomuser:randompass";
                String auth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
                headers.add(new BasicHeader("Authorization", auth));
            }

            // RestClientBuilder에 헤더 설정
            RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, "https"))
                    .setDefaultHeaders(headers.toArray(new BasicHeader[0]));

            // RestClient 생성
            RestClient restClient = builder.build();

            // RestClientTransport에 `X-Elastic-Product` 강제 설정
            RestClientTransport transport = new RestClientTransport(
                    restClient, new JacksonJsonpMapper()
            );

            return new ElasticsearchClient(transport);


        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid Elasticsearch URI: " + host, e);
        }

    }
}
