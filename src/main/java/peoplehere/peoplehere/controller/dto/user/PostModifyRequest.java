package peoplehere.peoplehere.controller.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.controller.dto.image.PostImageRequest;
import peoplehere.peoplehere.domain.Language;
import peoplehere.peoplehere.domain.UserLanguage;
import peoplehere.peoplehere.domain.enums.Gender;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostModifyRequest {
    // TODO: 민감한 유저 정보 업데이트 분리 구현
//    private String email;
//    private String password;
//    private String name;
//    private LocalDate birth;
//    private Gender gender;
    private PostImageRequest imageRequest;
    private List<Long> languages;
    private String address;
    private String job;
    private String almaMater;
    private String hobby;
    private String pet;
    private String favourite;
    private String content;
    // TODO: 여행 문답 구현
}
