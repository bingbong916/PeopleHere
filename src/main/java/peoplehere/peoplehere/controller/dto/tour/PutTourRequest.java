package peoplehere.peoplehere.controller.dto.tour;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PutTourRequest {
    private Long tourId;
    private String name;
    private int budget;
    private String startDate;
    private int time;
    private String imageUrl;
    private String content;
}
