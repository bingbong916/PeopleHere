package peoplehere.peoplehere.controller.dto.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PutPlaceResponse {
    private Long id;
    private String content;
    private String imageUrl;
    private String address;
    private Long tourId;

    public PutPlaceResponse(Long id, String content, String address) {
        this.id = id;
        this.content = content;
        this.address = address;
    }
}


