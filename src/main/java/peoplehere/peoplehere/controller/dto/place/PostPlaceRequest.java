package peoplehere.peoplehere.controller.dto.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPlaceRequest { // 구글맵 API 특성상 제약조건 불필요
    private String content;
    private String imageUrl;
    private String address;
    private Long tourId;
}