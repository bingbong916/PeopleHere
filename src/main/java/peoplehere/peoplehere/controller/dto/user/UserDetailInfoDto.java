package peoplehere.peoplehere.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailInfoDto extends UserInfoDto {
    private List<String> languages;

    public UserDetailInfoDto(Long userId, String userName, String userImage, List<String> languages) {
        super(userId, userName, userImage);
        this.languages = languages;
    }
}