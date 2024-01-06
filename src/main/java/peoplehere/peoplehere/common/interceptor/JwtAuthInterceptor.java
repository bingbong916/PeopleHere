package peoplehere.peoplehere.common.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import peoplehere.peoplehere.common.exception.jwt.bad_request.JwtNoTokenException;
import peoplehere.peoplehere.service.UserService;
import peoplehere.peoplehere.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.JWT_TOKEN_NOT_PROVIDED;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("[JwtAuthInterceptor.preHandle]");

        String accessToken = resolveAccessToken(request);
        jwtProvider.validateToken(accessToken); // 토큰 유효성 검증

        Claims claims = jwtProvider.parseToken(accessToken);
        String userId = claims.getSubject();
        request.setAttribute("userId", userId);
        return true;
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        validateToken(token);
        return token.substring(JWT_TOKEN_PREFIX.length());
    }

    private void validateToken(String token) {
        if (token == null || !token.startsWith(JWT_TOKEN_PREFIX)) {
            throw new JwtNoTokenException(JWT_TOKEN_NOT_PROVIDED);
        }
    }
}
