package peoplehere.peoplehere.controller.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import peoplehere.peoplehere.domain.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PostPhoneNumberUserRequest {

    @Email(message = "이메일 형식이 유효하지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Length(max = 50, message = "이메일은 최대 50자까지 가능합니다.")
    private String email;

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String phoneNumber;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$",
        message = "비밀번호는 특수문자나 숫자를 최소 1자 이상 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Length(min = 1, max = 30)
    private String firstName;

    @NotBlank(message = "성을 입력해주세요.")
    @Length(min = 1, max = 30)
    private String lastName;

    @NotNull(message = "생년월일을 입력해주세요")
    private LocalDate birth;

    @NotNull(message = "성별을 선택해주세요.")
    private Gender gender;

    public void resetPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}