package peoplehere.peoplehere.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLoginResponse {
    private long userId;
    private String jwtToken;
}
