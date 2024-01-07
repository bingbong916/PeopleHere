package peoplehere.peoplehere.util.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import peoplehere.peoplehere.common.exception.jwt.bad_request.JwtUnsupportedTokenException;
import peoplehere.peoplehere.common.exception.jwt.unauthorized.JwtMalformedTokenException;
import peoplehere.peoplehere.common.exception.jwt.unauthorized.JwtInvalidTokenException;
import peoplehere.peoplehere.common.exception.jwt.unauthorized.JwtExpiredTokenException;

import java.util.Date;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Component
public class JwtProvider {

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

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
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

}
