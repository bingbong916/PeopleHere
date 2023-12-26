package peoplehere.peoplehere.etc;

import peoplehere.peoplehere.domain.Place;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.dto.PlaceInfoDto;
import peoplehere.peoplehere.dto.tour.TourCreateDto;

import java.util.ArrayList;
import java.util.List;

public class DtoConverter {

    public Tour tourDtoConverter(TourCreateDto tourCreateDto) {
        return new Tour(
                tourCreateDto.getName(),
                tourCreateDto.getBudget(),
                tourCreateDto.getTime(),
                tourCreateDto.getImageUrl(),
                tourCreateDto.getContent()
        );
    }

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
