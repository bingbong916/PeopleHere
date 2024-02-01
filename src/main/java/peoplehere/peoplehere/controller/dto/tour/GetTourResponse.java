package peoplehere.peoplehere.controller.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTourResponse {
    private Long id;
    private String name;
    private Date startDate;
    private int time;
    private String imageUrl;
    private String content;
    private List<PlaceInfoDto> places;
    private List<String> categoryNames;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
