package peoplehere.peoplehere.controller.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourDatesInfoDto {
    private String tourName;
    private Long tourDateId;
    private LocalDateTime tourDateTime;
    private UserInfoDto oppositeUserInfo;
    private PlaceInfoDto firstPlaceInfo;
}
