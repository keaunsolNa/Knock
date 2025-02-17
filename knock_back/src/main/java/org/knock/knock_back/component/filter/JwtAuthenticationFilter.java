package org.knock.knock_back.component.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.knock.knock_back.component.config.JwtTokenProvider;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author nks
 * @apiNote Jwt Token Filter
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 JWT 가쟈오기
        String token = jwtTokenProvider.resolveToken(request);

        logger.info("[{}] TOKEN" + token);
        logger.info( request.getCookies());
        logger.info( request.getParameterMap());
        // 유효한 토큰인지 확인.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아오기.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
            return;
        }

        // TODO : 토큰 유효하지 않을 경우 Redirect 처리
        filterChain.doFilter(request, response);
//        response.sendRedirect("/login");

    }
}