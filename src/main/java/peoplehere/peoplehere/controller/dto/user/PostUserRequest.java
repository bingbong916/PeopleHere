package peoplehere.peoplehere.controller.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import peoplehere.peoplehere.domain.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PostUserRequest {

    @Email(message = "이메일 형식이 유효하지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Length(max = 50, message = "이메일은 최대 50자까지 가능합니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 8, max = 20, message = "비밀번호는 8 ~ 20자 사이여야 합니다.")
    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,20}",
            message = "비밀번호는 대문자, 소문자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Length(min = 2, max = 30, message = "이름은 2 ~ 30자 사이여야 합니다.")
    private String name;

    private Gender gender;

    @Nullable
    private String imageUrl;

    @Nullable
    @Length(max = 500, message = "500자 이내로 작성해주세요.")
    private String content; // 사용자의 자기소개나 추가 정보

    @Nullable
    private String address;

    @Nullable
    private LocalDate birth;

    @Nullable
    private String job;

    @Nullable
    private String almaMater;

    @Nullable
    private String hobby;

    @Nullable
    private String pet;

    @Nullable
    private String favourite;

    @Nullable
    private boolean leader; // true or false

    // TODO: 문답에 대한 필드 추가 예정
    public void resetPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}