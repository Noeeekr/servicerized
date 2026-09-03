package com.github.noeeekr.servicerized.identity.middlewares;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.github.noeeekr.servicerized.identity.repository.models.User;
import com.github.noeeekr.servicerized.identity.services.authentication.AuthenticationJwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationJwtService authenticationJwtService;

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest httpRequest,
            @NonNull HttpServletResponse httpResponse, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        final String token = authHeader.substring(7);
        User user = authenticationJwtService.getPayload(token);

        // Sets spring security authentication for this request
        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (authenticationJwtService.isTokenValid(token, user) == false) {
                filterChain.doFilter(httpRequest, httpResponse);
                return;
            }

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getName(), null, authorities);
            authenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(httpRequest, httpResponse);
    }
}
