package peoplehere.peoplehere.controller.dto.user;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourContentsUserInfoDto {
    private Long userId;
    private String userName;
    private String userImageUrl;

    private List<String> languages;
    private Map<String, String> questions;

}
