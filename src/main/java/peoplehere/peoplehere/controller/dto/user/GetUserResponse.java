package peoplehere.peoplehere.controller.dto.user;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.domain.Language;
import peoplehere.peoplehere.domain.UserLanguage;
import peoplehere.peoplehere.domain.enums.Gender;
import peoplehere.peoplehere.domain.enums.Status;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {
    private Long id;
    private String email;
    private String firstName;
    private Gender gender;
    private String imageUrl;
    private String content;
    private String address;
    private LocalDate birth;
    private String job;
    private String almaMater;
    private String hobby;
    private String pet;
    private String favourite;
    private Status status;
    private List<String> languages;
    // TODO: 문답에 대한 필드 추가 예정
}

