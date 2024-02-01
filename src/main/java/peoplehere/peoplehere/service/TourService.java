package peoplehere.peoplehere.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.common.exception.TourException;
import peoplehere.peoplehere.controller.dto.place.PlaceDtoConverter;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.controller.dto.tour.PostTourRequest;
import peoplehere.peoplehere.controller.dto.tour.PutTourRequest;
import peoplehere.peoplehere.controller.dto.tour.TourDtoConverter;
import peoplehere.peoplehere.domain.*;
import peoplehere.peoplehere.repository.CategoryRepository;
import peoplehere.peoplehere.repository.TourCategoryRepository;
import peoplehere.peoplehere.repository.TourRepository;
import peoplehere.peoplehere.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.TOUR_NOT_FOUND;
import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TourService {

    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TourCategoryRepository tourCategoryRepository;

    /**
     * 모든 투어 조회
     */
    @Transactional(readOnly = true)
    public Page<Tour> findAllTours(Pageable pageable) {
        return tourRepository.findAll(pageable);
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
            return tourRepository.findAll(pageable);
        } else {
            List<Category> categoryList = categoryRepository.findByNameIn(categories);
            return tourRepository.findByCategoriesIn(categoryList, pageable);
        }
    }

    /**
     * 투어 시작일 설정
     */
    public void setStartDate(Long id, Date startDate) {
        Tour findTour = tourRepository.findById(id).orElseThrow();
        findTour.setStartDate(startDate);
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
        List<Place> places = new ArrayList<>();
        int order = 1;
        for (PlaceInfoDto placeInfoDto : postTourRequest.getPlaces()) {
            Place place = PlaceDtoConverter.placeInfoDtoToPlace(placeInfoDto);
            place.setOrder(order++); // 순서를 리스트의 순서대로 할당
            place.setImageUrls(placeInfoDto.getImageUrls());
            place.setTour(tour);
            places.add(place);
        }
        tour.setPlaces(places);

        // 카테고리 정보 설정
        if (postTourRequest.getCategoryNames() != null && !postTourRequest.getCategoryNames().isEmpty()) {
            List<TourCategory> tourCategories = new ArrayList<>();
            for (String categoryName : postTourRequest.getCategoryNames()) {
                Category category = categoryRepository.findByName(categoryName);
                if (category != null) {
                    tourCategories.add(new TourCategory(tour, category));
                }
            }
            tour.setTourCategories(tourCategories);
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
     * 투어 삭제
     */
    public void deleteTour(Long id) {
        Tour tour = tourRepository.findById(id).orElseThrow();
        tour.setStatus("삭제");
        tourRepository.save(tour);
    }


}
