package peoplehere.peoplehere.controller.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.controller.dto.user.UserInfoDto;
import peoplehere.peoplehere.domain.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTourResponse {
    private Long id;
    private String name;
    private int time;
    private String content;
    private List<PlaceInfoDto> places;
    private List<String> categoryNames;
    private List<UserInfoDto> participants;
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
