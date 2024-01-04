package peoplehere.peoplehere.controller.dto.tour;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetToursResponse {
    private List<TourInfo> tours;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TourInfo {
        private Long id;
        private String name;
        private int budget;
        private String startDate;
        private String imageUrl;
        private String content;
    }
}