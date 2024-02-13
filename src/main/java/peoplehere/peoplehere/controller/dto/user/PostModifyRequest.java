package peoplehere.peoplehere.controller.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.controller.dto.image.PostImageRequest;
import peoplehere.peoplehere.domain.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PostModifyRequest {
//    private String email;
//    private String password;
//    private String name;
    private Gender gender;
    private String address;
    private LocalDate birth;
    private String job;
    private String almaMater;
    private String hobby;
    private String pet;
    private String favourite;
    private PostImageRequest imageRequest;
    private String content;
}
