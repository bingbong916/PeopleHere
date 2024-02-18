package peoplehere.peoplehere.controller.dto.tour;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.controller.dto.user.UserInfoDto;
import peoplehere.peoplehere.domain.enums.TourDateStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTourParticipantsResponse {
    private Long tourHistoryId;
    private Long tourDateId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
    private TourDateStatus status;
    private UserInfoDto participants;

}
