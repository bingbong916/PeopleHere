package peoplehere.peoplehere.controller.dto.place;

import java.util.ArrayList;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import peoplehere.peoplehere.domain.Place;



public class PlaceDtoConverter {

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326); // SRID 4326으로 설정

    public static Place placeInfoDtoToPlace(PlaceInfoDto placeInfoDto) {
        return new Place(placeInfoDto.getContent(),
                placeInfoDto.getImageUrls(),
                placeInfoDto.getAddress(),
                placeInfoDto.getLatLng(),
                placeInfoDto.getOrder()
        );
    }

    /**
     * @return 사진 정보가 담기지 않은 Place
     */
    public static Place postPlaceRequestToPlace(PostPlaceRequest postPlaceRequest){
        Point point = convertLatLngDtoToPoint(postPlaceRequest.getLatLng());

        return new Place(postPlaceRequest.getPlaceName(), new ArrayList<>(), postPlaceRequest.getPlaceAddress(),
            point, postPlaceRequest.getPlaceOrder());
    }

    private static Point convertLatLngDtoToPoint(LatLngDto latLngDto) {
        if (latLngDto == null) return null;
        return geometryFactory.createPoint(new Coordinate(latLngDto.getX(), latLngDto.getY()));
    }
    public static PostPlaceResponse placeToPostPlaceResponse(Place place) {
        return new PostPlaceResponse(place.getId(), place.getContent(), place.getImageUrls(),
            place.getAddress(),
            place.getOrder());
    }
}
