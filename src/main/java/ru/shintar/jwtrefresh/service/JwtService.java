package ru.shintar.jwtrefresh.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.shintar.jwtrefresh.exception.AccessDeniedException;
import ru.shintar.jwtrefresh.model.dto.JwtResponse;
import ru.shintar.jwtrefresh.model.entity.User;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String ROLE_CLAIM = "role";
    private static final String ID_CLAIM = "id";

    @Value("${jwt.secret.access}")
    private String jwtSecret;

    @Value("${jwt.expiration.access}")
    private Duration accessTokenExpiration;

    @Value("${jwt.expiration.refresh}")
    private Duration refreshTokenExpiration;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration, jwtSecret);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration, jwtSecret);
    }

    public String generateToken(User user, Duration tokenExpiration, String secret) {
        Date now = new Date();
        final Date expiration = new Date(now.getTime() + tokenExpiration.toMillis());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .issuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .claim(ROLE_CLAIM, user.getRoles())
                .claim(ID_CLAIM, user.getId())
                .compact();
    }

    public JwtResponse refreshToken(String refresh) {

        if (!validateToken(refresh)) {
            throw new AccessDeniedException();
        }
        String userName = getUsernameFromJWT(refresh);
        User user = userService.getByUsername(userName);
        final String accessToken = generateAccessToken(user);
        final String refreshToken = generateRefreshToken(user);

        return new JwtResponse(accessToken, refreshToken);
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    public String getUsernameFromJWT(String jwtToken) {
        return getClaimFromJWT(jwtToken, Claims::getSubject);
    }
    public <T> T getClaimFromJWT(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromJWT(jwtToken);
        return claimsResolver.apply(claims);
    }
    private Claims getAllClaimsFromJWT(String jwtToken) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }
    public Authentication getAuthentication(String token) {
        String userName = getUsernameFromJWT(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
