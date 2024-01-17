package peoplehere.peoplehere.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.domain.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {
    private String email;
    private String name;
    private Gender gender;
    private boolean leader;
    private String imageUrl;
    private String content;
    private String address;
    private LocalDate birth;
    private String job;
    private String almaMater;
    private String hobby;
    private String pet;
    private String favourite;
    // TODO: 문답에 대한 필드 추가 예정
}

