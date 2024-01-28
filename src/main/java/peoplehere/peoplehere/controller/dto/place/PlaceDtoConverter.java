package peoplehere.peoplehere.controller.dto.place;

import peoplehere.peoplehere.domain.Place;

public class PlaceDtoConverter {
    public static Place placeInfoDtoToPlace(PlaceInfoDto placeInfoDto) {
        return new Place(placeInfoDto.getContent(),
                placeInfoDto.getImageUrls(),
                placeInfoDto.getAddress(),
                placeInfoDto.getOrder()
        );
    }
}
