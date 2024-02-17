package peoplehere.peoplehere.controller.dto.tour;

import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.controller.dto.user.UserDetailInfoDto;
import peoplehere.peoplehere.domain.Language;
import peoplehere.peoplehere.domain.Place;
import peoplehere.peoplehere.domain.TourDate;
import peoplehere.peoplehere.domain.UserLanguage;

import java.util.Comparator;
import java.util.List;

public class TourDateDtoConverter {

    public static GetTourDatesResponse tourDateToGetTourDatesResponse(TourDate tourDate) {
        GetTourDatesResponse response = new GetTourDatesResponse();
        response.setTourDateId(tourDate.getId());
        response.setDate(tourDate.getDate());
        response.setTime(tourDate.getTime());
        response.setStatus(tourDate.getStatus());
        return response;
    }

    public static TourDateInfoDto convertToTourDateInfoDto(TourDate tourDate) {
        var tour = tourDate.getTour();
        var tourLeader = tour.getUser();

        List<PlaceInfoDto> places = tour.getPlaces().stream()
                .sorted(Comparator.comparingInt(Place::getOrder))
                .map(place -> new PlaceInfoDto(
                        place.getId(),
                        place.getContent(),
                        place.getImageUrls(),
                        place.getAddress(),
                        place.getLatLng(),
                        place.getOrder()))
                .toList();

        List<String> languages = tourLeader.getLanguages().stream()
                .map(UserLanguage::getLanguage)
                .map(Language::getKoreanName)
                .toList();

        UserDetailInfoDto tourLeaderInfo = new UserDetailInfoDto(
                tourLeader.getId(),
                tourLeader.getFirstName(),
                tourLeader.getImageUrl(),
                languages);

        return new TourDateInfoDto(
                tour.getId(),
                tour.getTime(),
                tourLeaderInfo,
                places,
                tourDate.getDate(),
                tourDate.getTime(),
                tourDate.getStatus());
    }
}
