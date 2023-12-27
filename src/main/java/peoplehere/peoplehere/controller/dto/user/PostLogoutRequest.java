package peoplehere.peoplehere.controller.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostLogoutRequest {
    private String email; // 또는 userId 등 로그아웃에 필요한 식별 정보
}
