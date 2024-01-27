package peoplehere.peoplehere.controller.dto.tour;

import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.domain.Tour;

import java.util.List;
import java.util.stream.Collectors;

public class TourDtoConverter {
    public static Tour postTourRequestToTour(PostTourRequest postTourRequest) {
        Tour tour = new Tour(postTourRequest.getName(), postTourRequest.getBudget(),
                postTourRequest.getTime(), postTourRequest.getImageUrl(),postTourRequest.getContent());
        return tour;

    }

    public static GetTourResponse tourToGetTourResponse(Tour tour) {
        GetTourResponse getTourResponse = new GetTourResponse();
        getTourResponse.setId(tour.getId());
        getTourResponse.setName(tour.getName());
        getTourResponse.setBudget(tour.getBudget());
        getTourResponse.setStartDate(tour.getStartDate());
        getTourResponse.setImageUrl(tour.getImageUrl());
        getTourResponse.setContent(tour.getContent());

        // 투어에 속한 장소들을 PlaceInfoDto로 변환하여 추가
        List<PlaceInfoDto> placeInfoDtos = tour.getPlaces().stream()
                .map(place -> new PlaceInfoDto(place.getId(), place.getContent(), place.getImageUrl(), place.getAddress(), place.getOrder()))
                .collect(Collectors.toList());
        getTourResponse.setPlaces(placeInfoDtos);

        return getTourResponse;
    }
}