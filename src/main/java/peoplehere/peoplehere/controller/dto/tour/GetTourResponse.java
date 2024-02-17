package peoplehere.peoplehere.controller.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.controller.dto.user.UserInfoDto;
import peoplehere.peoplehere.domain.enums.Status;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTourResponse {
    private Long tourId;
    private String tourName;
    private boolean isWished;
    private Long userId;
    private String userName;
    private String userImageUrl;
    private int tourTime;
    private String tourContent;
    private List<PlaceInfoDto> places;
    private List<String> categoryNames;
    private List<UserInfoDto> participants;
    private Status status;
}
