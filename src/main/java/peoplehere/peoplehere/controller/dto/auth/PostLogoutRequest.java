package peoplehere.peoplehere.controller.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostLogoutRequest {
    private String email;
    // TODO: 인증 방식 선정 후 삭제 예정
}
