package peoplehere.peoplehere.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.common.exception.TourException;
import peoplehere.peoplehere.controller.dto.place.PlaceDtoConverter;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.controller.dto.tour.GetTourDatesResponse;
import peoplehere.peoplehere.controller.dto.place.PostPlaceResponse;
import peoplehere.peoplehere.controller.dto.tour.PostTourRequest;
import peoplehere.peoplehere.controller.dto.tour.PutTourRequest;
import peoplehere.peoplehere.controller.dto.tour.TourDtoConverter;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.domain.*;
import peoplehere.peoplehere.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TourService {

    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TourCategoryRepository tourCategoryRepository;
    private final PlaceRepository placeRepository;
    private final TourHistoryRepository tourHistoryRepository;
    private final TourDateRepository tourDateRepository;

    /**
     * 모든 투어 조회
     */
    @Transactional(readOnly = true)
    public Page<Tour> findAllTours(Pageable pageable) {
        return tourRepository.findAllByStatus(Status.ACTIVE, pageable);
    }

    /**
     * 특정 투어 조회
     */
    @Transactional(readOnly = true)
    public Tour findTourById(Long id) {
        return tourRepository.findById(id).orElseThrow();
    }

    /**
     * 특정 카테고리에 해당하는 투어 조회
     */
    @Transactional(readOnly = true)
    public Page<Tour> findAllToursByCategory(List<String> categories, Pageable pageable) {
        if (categories == null || categories.isEmpty()) {
            return tourRepository.findAllByStatus(Status.ACTIVE, pageable);
        } else {
            List<Category> categoryList = categoryRepository.findByNameIn(categories);
            return tourRepository.findByCategoriesInAndStatus(categoryList, Status.ACTIVE, pageable);
        }
    }

    /**
     * 특정 투어의 모든 일정 조회
     */
    public List<GetTourDatesResponse> getTourDates(Long tourId) {
        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new TourException(TOUR_NOT_FOUND));
        return tour.getTourDates().stream()
                .map(TourDtoConverter::tourDateToGetTourDatesResponse)
                .collect(Collectors.toList());
    }

    /**
     * 투어 생성
     */
    public Tour createTour(PostTourRequest postTourRequest) {
        Tour tour = TourDtoConverter.postTourRequestToTour(postTourRequest);
        // 사용자 확인
        User user = userRepository.findById(postTourRequest.getUserId())
                .orElseThrow(() -> new TourException(USER_NOT_FOUND));
        tour.setUser(user);

        // 장소 설정
        List<PostPlaceResponse> postTourRequestPlaces = postTourRequest.getPlaces();
        List<Place> places = new ArrayList<>();
        for (PostPlaceResponse postTourRequestPlace : postTourRequestPlaces) {
            Place place = placeRepository.findById(postTourRequestPlace.getId()).orElseThrow();
            place.setTour(tour);
            places.add(place);
        }
        tour.setPlaces(places);

        // 카테고리 정보 설정
        if (postTourRequest.getCategoryNames() != null && !postTourRequest.getCategoryNames().isEmpty()) {
            List<Category> categories = categoryRepository.findByNameIn(postTourRequest.getCategoryNames());
            categories.forEach(category -> {
                if (tour.getTourCategories().stream().noneMatch(tc -> tc.getCategory().equals(category))) {
                    tour.getTourCategories().add(new TourCategory(tour, category));
                }
            });
        }
        tourRepository.save(tour);

        return tour;
    }

    /**
     * 투어 수정
     */
    public void modifyTour(Long id, PutTourRequest putTourRequest) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new TourException(TOUR_NOT_FOUND));
        // 투어 수정
        tour.update(putTourRequest);

        // 투어에 속한 장소 수정
        if (putTourRequest.getPlaces() != null && !putTourRequest.getPlaces().isEmpty()) {
            updatePlaces(tour, putTourRequest.getPlaces());
        }

        // 삭제할 장소가 있는 경우 처리
        if (putTourRequest.getDeletedPlaceIds() != null && !putTourRequest.getDeletedPlaceIds().isEmpty()) {
            deletePlaces(tour, putTourRequest.getDeletedPlaceIds());
        }

        // 투어 카테고리 수정
        updateTourCategories(tour, putTourRequest.getCategoryNames());

        tourRepository.save(tour);
    }

    private void updatePlaces(Tour tour, List<PlaceInfoDto> placeInfoDtos) {
        List<Place> updatedPlaces = new ArrayList<>();
        int order = 1;

        for (PlaceInfoDto placeInfoDto : placeInfoDtos) {
            Place place = tour.getPlaces().stream()
                    .filter(p -> p.getId() != null && p.getId().equals(placeInfoDto.getId()))
                    .findFirst()
                    .orElseGet(() -> {
                        Place newPlace = PlaceDtoConverter.placeInfoDtoToPlace(placeInfoDto);
                        newPlace.setTour(tour);
                        return newPlace;
                    });

            place.update(placeInfoDto);
            place.setOrder(order++);
            updatedPlaces.add(place);
        }

        tour.setPlaces(updatedPlaces);
    }

    private void deletePlaces(Tour tour, List<Long> deletedPlaceIds) {
        tour.getPlaces().removeIf(place -> deletedPlaceIds.contains(place.getId()));
        reorderPlaces(tour);
    }

    private void reorderPlaces(Tour tour) {
        int order = 1;
        for (Place place : tour.getPlaces()) {
            place.setOrder(order++);
        }
    }

    private void updateTourCategories(Tour tour, List<String> categoryNames) {
        // 기존 카테고리 연결 제거 및 새 카테고리 연결
        tourCategoryRepository.deleteAll(tour.getTourCategories());
        tour.getTourCategories().clear();
        categoryNames.forEach(categoryName -> {
            Category category = categoryRepository.findByName(categoryName);
            if (category != null) {
                tour.getTourCategories().add(new TourCategory(tour, category));
            }
        });
    }

    /**
     * 상태 변경
     */
    public void updateTourStatus(Long id, Status status) {
        Tour tour = tourRepository.findById(id).orElseThrow(() -> new TourException(TOUR_NOT_FOUND));
        tour.setStatus(status);
        tourRepository.save(tour);
    }

    /**
     * 투어 삭제
     */
    public void deleteTour(Long id) {
        Tour tour = tourRepository.findById(id).orElseThrow();
        tour.setStatus(Status.DELETED);
        tourRepository.save(tour);
    }

    /**
     * 투어 참여
     */
    public TourHistory joinTour(Long tid, Long uid) {
        Tour tour = tourRepository.findById(tid)
                .orElseThrow(() -> new TourException(TOUR_NOT_FOUND));
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new TourException(USER_NOT_FOUND));

        // 투어 상태가 ACTIVE가 아닌 경우 참여 불가
        if (tour.getStatus() != Status.ACTIVE) {
            throw new TourException(TOUR_INACTIVATED);
        }

        // 이미 참여한 경우 중복 참여 방지
        boolean alreadyJoined = tour.getTourHistories().stream()
                .anyMatch(th -> th.getUser().equals(user));
        if (alreadyJoined) {
            throw new TourException(TOUR_ALREADY_JOINED);
        }

        TourHistory tourHistory = new TourHistory();
        tourHistory.setUser(user);
        tourHistory.setTour(tour);
        tourHistory.setStatus("예약중"); //TODO: Status 상수화 (예약중, 취소됨, 등)

        return tourHistoryRepository.save(tourHistory);
    }

    /**
     * 투어 일정 추가
     */
    @Transactional
    public void addOrUpdateTourDate(Long tourId, LocalDate date, LocalTime time) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new TourException(TOUR_NOT_FOUND));

        validateTourDate(date, time);

        // 해당 날짜에 대한 TourDate 객체 찾기
        Optional<TourDate> existingTourDate = tour.getTourDates().stream()
                .filter(td -> td.getDate().equals(date))
                .findFirst();


        existingTourDate.ifPresentOrElse(
                tourDate -> {
                    if (time != null) {
                        tourDate.setTime(time);
                    }
                },
                () -> {
                    TourDate newTourDate = new TourDate();
                    newTourDate.setDate(date);
                    newTourDate.setTime(time);
                    newTourDate.makeAvailable();
                    tour.addTourDate(newTourDate);
                    tourDateRepository.save(newTourDate);
                }
        );
    }

    /**
     * 투어 일정 삭제
     */
    public void removeTourDate(Long tourDateId) {
        TourDate tourDate = tourDateRepository.findById(tourDateId)
                .orElseThrow(() -> new TourException(TOUR_DATE_NOT_FOUND));

        tourDateRepository.delete(tourDate);
    }

    private void validateTourDate(LocalDate date, LocalTime time) {

        LocalDateTime localDateTime = LocalDateTime.of(date, time);

        // 날짜가 과거인지 확인
        if (localDateTime.isBefore(LocalDateTime.now())) {
            throw new TourException(TOUR_DATE_IN_PAST);
        }
    }
}
