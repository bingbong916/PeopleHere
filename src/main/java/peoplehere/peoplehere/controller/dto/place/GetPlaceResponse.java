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
    private Long tourId;
    private Long placeId;
    private List<String> placeImages;
    private String placeAddress;
    private int placeOrder;
}
