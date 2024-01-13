package peoplehere.peoplehere.util.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import peoplehere.peoplehere.common.exception.jwt.bad_request.JwtUnsupportedTokenException;
import peoplehere.peoplehere.common.exception.jwt.unauthorized.JwtMalformedTokenException;
import peoplehere.peoplehere.common.exception.jwt.unauthorized.JwtInvalidTokenException;
import peoplehere.peoplehere.common.exception.jwt.unauthorized.JwtExpiredTokenException;
import peoplehere.peoplehere.service.UserDetailsServiceImpl;

import java.security.Key;
import java.util.Date;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserDetailsServiceImpl userDetailsServiceimpl;

    @Value("${secret.jwt-secret-key}")
    private String jwtSecretKey;

    @Value("${secret.jwt-expired-in}")
    private long jwtExpiredIn;

    @Value("${secret.jwt-refresh-secret-key}")
    private String jwtRefreshSecretKey;

    @Value("${secret.jwt-refresh-expired-in}")
    private long jwtRefreshExpiredIn;

    public String createAccessToken(Long userId) {
        return createToken(userId, jwtExpiredIn, jwtSecretKey);
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, jwtRefreshExpiredIn, jwtRefreshSecretKey);
    }

    private String createToken(Long userId, long expirationTime, String secretKey) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime);
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey).build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredTokenException(JWT_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new JwtUnsupportedTokenException(JWT_TOKEN_UNSUPPORTED);
        } catch (MalformedJwtException e) {
            throw new JwtMalformedTokenException(JWT_TOKEN_MALFORMED);
        } catch (IllegalArgumentException | JwtException e) {
            throw new JwtInvalidTokenException(JWT_INVALID_TOKEN);
        }
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey).build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            throw new JwtInvalidTokenException(JWT_INVALID_TOKEN);
        }
    }

    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = userDetailsServiceimpl.loadUserByUsername(parseToken(accessToken).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
