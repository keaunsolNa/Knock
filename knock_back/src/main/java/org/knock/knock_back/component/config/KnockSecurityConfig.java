package org.knock.knock_back.component.config;

import lombok.RequiredArgsConstructor;
import org.knock.knock_back.component.filter.EncodingFilter;
import org.knock.knock_back.component.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.knock.knock_back.component.util.converter.JwtKeyConverter;

import java.util.List;

/**
 * @author nks
 * @apiNote Spring Security Filter Chain
 */
@RequiredArgsConstructor
@Configuration
public class KnockSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * SecurityFilterChain 을 통해 Spring Security 인증 Filter 생성한다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // HTTP Basic 인증을 사용하지 않도록 비활성화
                // TODO : HTTPS 연결
                .httpBasic(AbstractHttpConfigurer::disable)
                // CSRF 보호 비활성화
                // TODO : 보호하고, Front 요청만 허용
                .csrf((AbstractHttpConfigurer::disable))
                // 세션을 설정하지 않도록 (매 요청마다 JWT 검증)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 요청 권한
                // TODO : 모든 요청 -> URL 명세 확립 후 결정
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll()
                )
                // OAuth2 리소스 서버 설정 (JWT 인증 방식 사용)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtKeyConverter()))
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)  // JWT 필터 추가
                .addFilterAfter(new EncodingFilter(), JwtAuthenticationFilter.class)    // JWT 필터 이후 Encoding 필터
                ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 허용할 Origin
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
