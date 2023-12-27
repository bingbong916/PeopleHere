package peoplehere.peoplehere.dto.tour;

import lombok.Getter;
import peoplehere.peoplehere.dto.PlaceInfoDto;

import java.util.List;

@Getter
public class TourCreateDto {

    private Long userId;
    private String name;
    private int budget;
    private int time;
    private String imageUrl;
    private String content;
    private List<String> categoryNames;
    private List<PlaceInfoDto> places;

    public TourCreateDto(Long userId, String name, int budget, int time, String imageUrl, String content, List<String> categoryNames, List<PlaceInfoDto> places) {
        this.userId = userId;
        this.name = name;
        this.budget = budget;
        this.time = time;
        this.imageUrl = imageUrl;
        this.content = content;
        this.categoryNames = categoryNames;
        this.places = places;
    }
}
