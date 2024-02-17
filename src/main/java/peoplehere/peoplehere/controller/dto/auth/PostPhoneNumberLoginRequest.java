package peoplehere.peoplehere.controller.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostPhoneNumberLoginRequest {

    @NotBlank(message = "휴대폰 번호를 입력해주세요")
    private String phoneNumber;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}