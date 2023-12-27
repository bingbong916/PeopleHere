package peoplehere.peoplehere.controller.dto.tour;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.controller.dto.place.PostPlaceResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostTourRequest {
    private Long userId;
    private String name;
    private int budget;
    private int time;
    private String imageUrl;
    private String content;
    private List<PostPlaceResponse> PostPlaceResponse;
    // private List<> places;
}
