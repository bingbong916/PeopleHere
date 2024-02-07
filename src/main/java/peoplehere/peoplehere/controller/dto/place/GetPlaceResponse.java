package peoplehere.peoplehere.controller.dto.place;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlaceResponse {
    private Long id;
    private String content;
    private List<String> imageUrls;
    private String address;
    private int order;
    private Long tourId;
}
