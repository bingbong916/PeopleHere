package peoplehere.peoplehere.controller.dto.tour;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.controller.dto.user.UserDetailInfoDto;
import peoplehere.peoplehere.domain.enums.TourDateStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourDateInfoDto {
    private Long tourId;
    private String tourName;
    private int tourTime;
    private UserDetailInfoDto tourLeader;
    private List<PlaceInfoDto> places;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
    private TourDateStatus status;
}
