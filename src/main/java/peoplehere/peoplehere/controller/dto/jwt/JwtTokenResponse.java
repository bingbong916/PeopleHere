package peoplehere.peoplehere.controller.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenResponse {

    private String accessToken;
    private String refreshToken;
}
