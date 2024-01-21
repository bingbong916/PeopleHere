package peoplehere.peoplehere.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
    public List<Tour> findAllTours() {
        return tourRepository.findAll();
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
    public List<Tour> findAllToursByCategory(List<String> categories) {
        List<Category> categoryList = new ArrayList<>();
        for (String category : categories) {
            categoryList.add(categoryRepository.findByName(category));
        }
        List<Tour> findTours = tourRepository.findByCategoryIn(categoryList);
        return findTours;
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
        User user = userRepository.findById(postTourRequest.getUserId()).orElseThrow();
        tour.setUser(user);

        List<Place> places = new ArrayList<>();
        for (PlaceInfoDto placeInfoDto : postTourRequest.getPlaces()) {
            Place place = PlaceDtoConverter.placeInfoDtoToPlace(placeInfoDto);
            place.setOrder(placeInfoDto.getOrder()); // 순서 설정
            place.setTour(tour);
            places.add(place);
        }
        tour.setPlace(places);
        tourRepository.save(tour);

        List<String> categoryNames = postTourRequest.getCategoryNames();
        for (String categoryName : categoryNames) {
            Category findCategory = categoryRepository.findByName(categoryName);
            TourCategory tourCategory = new TourCategory(tour, findCategory);
            tourCategoryRepository.save(tourCategory);
        }

        return tour;
    }

    /**
     * 투어 수정
     */
    public void modifyTour(Long id, PutTourRequest putTourRequest) {
        Tour tour = tourRepository.findById(id).orElseThrow();
        // 투어 수정
        tour.update(putTourRequest);
        // 투어에 속한 장소 수정
        if (putTourRequest.getPlaces() != null && !putTourRequest.getPlaces().isEmpty()) {
            updatePlaces(tour, putTourRequest.getPlaces());
        }

        if (putTourRequest.getDeletedPlaceIds() != null && !putTourRequest.getDeletedPlaceIds().isEmpty()) {
            deletePlaces(tour, putTourRequest.getDeletedPlaceIds());
        }

        tourRepository.save(tour);
    }

    private void updatePlaces(Tour tour, List<PlaceInfoDto> placeInfoDtos) {
        List<Place> updatedPlaces = new ArrayList<>();

        for (PlaceInfoDto placeInfoDto : placeInfoDtos) {
            Place existingPlace = tour.getPlaces().stream()
                    .filter(p -> p.getId().equals(placeInfoDto.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingPlace != null) {
                // 기존 장소 업데이트
                existingPlace.update(placeInfoDto);

            } else {
                // 새로운 장소 추가
                Place newPlace = PlaceDtoConverter.placeInfoDtoToPlace(placeInfoDto);
                newPlace.setTour(tour);
                updatedPlaces.add(newPlace);
            }

            // 기존 장소 목록 업데이트
            tour.getPlaces().clear();
            tour.getPlaces().addAll(updatedPlaces);
            reorderPlaces(tour);
        }
    }
    private void deletePlaces(Tour tour, List<Long> deletedPlaceIds) {
        tour.getPlaces().removeIf(place -> deletedPlaceIds.contains(place.getId()));
    }

    private void reorderPlaces(Tour tour) {
        // 장소를 순서에 따라 정렬
        tour.getPlaces().sort(Comparator.comparing(Place::getOrder));

        // 순서 재할당
        int order = 1;
        for (Place place : tour.getPlaces()) {
            place.setOrder(order++);
        }
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
