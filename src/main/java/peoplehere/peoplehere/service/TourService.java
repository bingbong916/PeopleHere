package peoplehere.peoplehere.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.domain.*;
import peoplehere.peoplehere.controller.dto.tour.TourCreateDto;
import peoplehere.peoplehere.dto.tour.TourModifyDto;
import peoplehere.peoplehere.etc.DtoConverter;
import peoplehere.peoplehere.repository.CategoryRepository;
import peoplehere.peoplehere.repository.TourCategoryRepository;
import peoplehere.peoplehere.repository.TourRepository;
import peoplehere.peoplehere.repository.UserRepository;

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
     * 카테고리로 투어 검색
     */
    @Transactional(readOnly = true)
    public List<Tour> findAllToursByCategory(List<Category> categories) {
        List<Tour> findTours = tourRepository.findByCategoryIn(categories);
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
    public Tour createTour(TourCreateDto tourCreateDto) {
        DtoConverter dtoConverter = new DtoConverter();
        Tour tour = dtoConverter.tourDtoConverter(tourCreateDto);
        User user = userRepository.findById(tourCreateDto.getUserId()).orElseThrow();
        tour.setUser(user);
        List<Place> places = dtoConverter.placeDtoConverter(tourCreateDto.getPlaces());
        tour.setPlace(places);
        tourRepository.save(tour);

        List<String> categoryNames = tourCreateDto.getCategoryNames();
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
    public Tour modifyTour(Long id, TourModifyDto tourModifyDto) {
        Tour tour = tourRepository.findById(id).orElseThrow();
        tour.update(tourModifyDto);
        return tour;
    }

    /**
     * 투어 삭제
     */
    public void deleteTour(Long id) {
        Tour tour = tourRepository.findById(id).orElseThrow();
        tour.changeStatusToDelete();
    }


}
