package peoplehere.peoplehere.controller.dto.place;

import java.util.ArrayList;
import peoplehere.peoplehere.domain.Place;

public class PlaceDtoConverter {
    public static Place placeInfoDtoToPlace(PlaceInfoDto placeInfoDto) {
        return new Place(placeInfoDto.getContent(),
                placeInfoDto.getImageUrls(),
                placeInfoDto.getAddress(),
                placeInfoDto.getOrder()
        );
    }

    /**
     * @return 사진 정보가 담기지 않은 Place
     */
    public static Place postPlaceRequestToPlace(PostPlaceRequest postPlaceRequest){
        return new Place(postPlaceRequest.getContent(), new ArrayList<String>(), postPlaceRequest.getAddress(),
            postPlaceRequest.getOrder());
    }

    public static PostPlaceResponse placeToPostPlaceResponse(Place place) {
        return new PostPlaceResponse(place.getId(), place.getContent(), place.getImageUrls(),
            place.getAddress(),
            place.getOrder());
    }
}
