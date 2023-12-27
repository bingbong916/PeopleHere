package peoplehere.peoplehere.controller.dto.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlacesResponse {
    private List<PlaceInfo> places;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceInfo {
        private Long id;
        private String content;
        private String imageUrl;
        private String address;
        private Long tourId;
    }
}