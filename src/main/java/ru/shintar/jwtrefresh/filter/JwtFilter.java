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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import ru.shintar.jwtrefresh.model.entity.User;
import ru.shintar.jwtrefresh.service.JwtService;
import ru.shintar.jwtrefresh.service.UserService;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final String[] allowedPaths = {"swagger", "auth", "api-docs"};

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    @SneakyThrows
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        if (Arrays.stream(allowedPaths).anyMatch(it -> httpRequest.getRequestURI().contains(it))) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        final String token = getTokenFromRequest(httpRequest);

        try {
            if (token != null && jwtService.validateToken(token)) {
                String username = jwtService.getUsernameFromJWT(token);
                User user = userService.loadUserByUsername(username);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);

            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.setStatus(401);
                httpResponse.getWriter().println("No token");
                return;
            }
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException |
                 ExpiredJwtException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(401);
            httpResponse.getWriter().println("Invalid token");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(HEADER_NAME);
        if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
            return bearer.substring(7);
        }
        return null;
    }
}