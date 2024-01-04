package peoplehere.peoplehere.etc;

import peoplehere.peoplehere.controller.dto.user.GetUserResponse;
import peoplehere.peoplehere.domain.Place;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.controller.dto.tour.TourCreateDto;
import peoplehere.peoplehere.domain.User;

import java.util.ArrayList;
import java.util.List;

public class DtoConverter {



    public List<Place> placeDtoConverter(List<PlaceInfoDto> places) {
        ArrayList<Place> placeList = new ArrayList<>();

        //TODO: 장소 dtoConverter 만들기
//        for (PlaceInfoDto placeInfoDto : places) {
//            placeList.add(new Place(
//
//            ))
//        }
        return placeList;
    }

}
