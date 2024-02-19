package peoplehere.peoplehere.controller.dto.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.controller.dto.image.PostImageRequest;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PutPlaceRequest {
    private Long placeId;
    private String placeName;
    private List<PostImageRequest> placeImage;
    private String placeAddress;
    private LatLngDto latLng;
    private int placeOrder;
}
