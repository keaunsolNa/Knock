package org.knock.knock_back.component.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.knock.knock_back.component.config.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationHeaderFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        System.out.println("JWT Authentication Filter");
        String authorizationHeader = request.getHeader("Authorization");

        System.out.println(authorizationHeader);

        if (authorizationHeader == null) {
            response.sendError(401);
            return;
        }

        // "Bearer " 이후의 토큰 값만 추출
        String token = authorizationHeader.replace("Bearer ", "");
        logger.info(token);
        if (!jwtTokenProvider.validateToken(token)) {
            response.sendError(401);
        }

        else
        {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // SecurityContext 에 Authentication 객체를 저장.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
    }
}
