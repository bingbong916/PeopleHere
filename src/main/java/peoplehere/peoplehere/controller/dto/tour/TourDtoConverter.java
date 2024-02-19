package peoplehere.peoplehere.controller.dto.tour;

import jakarta.annotation.Nullable;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.controller.dto.user.UserInfoDto;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.domain.Place;
import peoplehere.peoplehere.domain.TourDate;
import peoplehere.peoplehere.domain.enums.TourHistoryStatus;

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

    // 주소에서 '~구' 또는 '~시'만 추출하는 메소드
    public static String extractDistrictOrCity(String address) {
        // '구' 단위로 주소가 끝나는 부분을 찾아 해당 부분만 반환
        int districtIndex = address.lastIndexOf("구");
        if (districtIndex != -1) { // '구'가 발견되면
            return address.substring(address.substring(0, districtIndex).lastIndexOf(" ") + 1, districtIndex + 1);
        }

        // '구' 단위가 없을 경우, '시' 단위로 주소를 잘라내어 반환
        int cityIndex = address.lastIndexOf("시");
        if (cityIndex != -1) { // '시'가 발견되면
            return address.substring(address.substring(0, cityIndex).lastIndexOf(" ") + 1, cityIndex + 1);
        }
        // '구'나 '시' 단위가 없는 경우, 전체 주소 반환
        return address;
    }

    public static GetTourResponse tourToGetTourResponse(Tour tour, boolean isWished) {
        GetTourResponse getTourResponse = new GetTourResponse();
        getTourResponse.setTourId(tour.getId());
        getTourResponse.setTourName(tour.getName());
        getTourResponse.setWished(isWished);
        getTourResponse.setUserId(tour.getUser().getId());
        getTourResponse.setUserName(tour.getUser().getFirstName());
        getTourResponse.setUserImageUrl(tour.getUser().getImageUrl());
        getTourResponse.setTourTime(tour.getTime());
        getTourResponse.setTourContent(tour.getContent());

        // 투어에 속한 장소들을 PlaceInfoDto로 변환하여 추가
        List<PlaceInfoDto> placeInfoDtos = tour.getPlaces().stream()
                .sorted(Comparator.comparingInt(Place::getOrder))
                .map(place -> new PlaceInfoDto(place.getId(), place.getContent(), place.getImageUrls(), extractDistrictOrCity(place.getAddress()), place.getLatLng(), place.getOrder()))
                .toList();
        getTourResponse.setPlaces(placeInfoDtos);

        // 투어의 카테고리 이름을 추출하여 추가
        List<String> categoryNames = tour.getTourCategories().stream()
                .map(tc -> tc.getCategory().getName())
                .toList();
        getTourResponse.setCategoryNames(categoryNames);

        // 투어의 참여 유저 리스트 추가
        List<UserInfoDto> participants = tour.getTourHistories().stream()
                .filter(tourHistory -> tourHistory.getStatus() == TourHistoryStatus.CONFIRMED)
                .map(th -> new UserInfoDto(th.getUser().getId(), th.getUser().getFirstName(), th.getUser().getImageUrl()))
                .collect(Collectors.toMap(
                        UserInfoDto::getUserId,
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