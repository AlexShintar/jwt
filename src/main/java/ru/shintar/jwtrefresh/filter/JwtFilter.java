package ru.shintar.jwtrefresh.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.filter.GenericFilterBean;
import ru.shintar.jwtrefresh.service.JwtService;

import java.util.Arrays;



@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final String[] allowedPaths = {"swagger", "auth"};

    private final JwtService jwtService;

    @Override
    @SneakyThrows
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String bearerToken = httpRequest.getHeader(HEADER_NAME);

        if (Arrays.stream(this.allowedPaths).anyMatch(it -> httpRequest.getRequestURI().contains(it))) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try {
            if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
                bearerToken = bearerToken.substring(7);
                if (jwtService.validateToken(bearerToken)) {
                    Authentication authentication = jwtService.getAuthentication(bearerToken);
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } else {
                var httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.setStatus(401);
                httpResponse.getWriter().println("No JWT token");
                return;
            }
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException | ExpiredJwtException e) {
            var httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(401);
            httpResponse.getWriter().println("Invalid JWT token");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}