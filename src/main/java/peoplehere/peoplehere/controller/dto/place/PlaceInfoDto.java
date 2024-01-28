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
public class PlaceInfoDto {
    private Long id;
    private String content;
    private List<String> imageUrls;
    private String address;
    private int order;
}
