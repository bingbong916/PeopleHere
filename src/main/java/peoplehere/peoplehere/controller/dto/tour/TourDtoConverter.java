package peoplehere.peoplehere.controller.dto.tour;

import jakarta.annotation.Nullable;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.controller.dto.user.UserInfoDto;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.domain.Place;
import peoplehere.peoplehere.domain.TourDate;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TourDtoConverter {
    public static Tour postTourRequestToTour(PostTourRequest postTourRequest) {
        Tour tour = new Tour(postTourRequest.getTourName(),
                postTourRequest.getTourTime(),postTourRequest.getTourContent());
        return tour;

    }

    public static GetTourResponse tourToGetTourResponse(Tour tour, boolean isWished) {
        GetTourResponse getTourResponse = new GetTourResponse();
        getTourResponse.setTourId(tour.getId());
        getTourResponse.setTourName(tour.getName());
        getTourResponse.setWished(isWished);
        getTourResponse.setUserId(tour.getUser().getId());
        getTourResponse.setUserName(tour.getUser().getFirstName());
        getTourResponse.setUserImageUrl(tour.getUser().getImageUrl());
        getTourResponse.setTime(tour.getTime());
        getTourResponse.setContent(tour.getContent());

        // 투어에 속한 장소들을 PlaceInfoDto로 변환하여 추가
        List<PlaceInfoDto> placeInfoDtos = tour.getPlaces().stream()
                .sorted(Comparator.comparingInt(Place::getOrder))
                .map(place -> new PlaceInfoDto(place.getId(), place.getContent(), place.getImageUrls(), place.getAddress(), place.getLatLng(), place.getOrder()))
                .toList();
        getTourResponse.setPlaces(placeInfoDtos);

        // 투어의 카테고리 이름을 추출하여 추가
        List<String> categoryNames = tour.getTourCategories().stream()
                .map(tc -> tc.getCategory().getName())
                .toList();
        getTourResponse.setCategoryNames(categoryNames);

        // 투어의 참여 유저 리스트 추가
        List<UserInfoDto> participants = tour.getTourHistories().stream()
                .map(th -> new UserInfoDto(th.getUser().getId(), th.getUser().getFirstName(), th.getUser().getImageUrl()))
                .collect(Collectors.toMap(
                        UserInfoDto::getId,
                        Function.identity(),
                        (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
        getTourResponse.setParticipants(participants);

        // 투어 상태, 생성 및 수정 날짜 설정
        getTourResponse.setStatus(tour.getStatus());

        return getTourResponse;
    }
}