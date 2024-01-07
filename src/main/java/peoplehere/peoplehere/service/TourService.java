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
        List<PlaceInfoDto> placeInfoDtos = postTourRequest.getPlaces();
        for (PlaceInfoDto placeInfoDto : placeInfoDtos) {
            Place place = PlaceDtoConverter.placeInfoDtoToPlace(placeInfoDto);
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
    public Tour modifyTour(Long id, PutTourRequest putTourRequest) {
        Tour tour = tourRepository.findById(id).orElseThrow();
        tour.update(putTourRequest);
        tourRepository.save(tour);
        return tour;
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
