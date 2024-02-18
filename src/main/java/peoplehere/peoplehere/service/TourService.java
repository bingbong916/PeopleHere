package peoplehere.peoplehere.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.common.exception.TourException;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.controller.dto.place.PlaceDtoConverter;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import peoplehere.peoplehere.controller.dto.place.PostPlaceRequest;
import peoplehere.peoplehere.controller.dto.place.PostPlaceResponse;
import peoplehere.peoplehere.controller.dto.tour.GetTourResponse;
import peoplehere.peoplehere.controller.dto.tour.PostTourRequest;
import peoplehere.peoplehere.controller.dto.tour.PutTourRequest;
import peoplehere.peoplehere.controller.dto.tour.TourDtoConverter;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.domain.*;
import peoplehere.peoplehere.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import peoplehere.peoplehere.util.security.UserDetailsImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final WishlistRepository wishlistRepository;
    private final PlaceService placeService;


    /**
     * 모든 투어 조회
     */
    @Transactional(readOnly = true)
    public Page<GetTourResponse> findAllTours(Authentication authentication, Pageable pageable) {
        Optional<User> user = extractUser(authentication);

        Page<Tour> tours = tourRepository.findAllByStatus(Status.ACTIVE, pageable);
        return tours.map(tour -> TourDtoConverter.tourToGetTourResponse(tour,
            user.map(u -> wishlistRepository.findByUserAndTour(u, tour).isPresent())
                .orElse(false)));
    }

    /**
     * 특정 투어 조회
     */
    @Transactional(readOnly = true)
    public GetTourResponse findTourById(Authentication authentication, Long id) {
        Optional<User> user = extractUser(authentication);

        Tour tour = tourRepository.findById(id)
            .orElseThrow(() -> new TourException(TOUR_NOT_FOUND));
        return TourDtoConverter.tourToGetTourResponse(tour,
            user.map(u -> wishlistRepository.findByUserAndTour(u, tour).isPresent()).orElse(false));
    }

    /**
     * 특정 카테고리에 해당하는 투어 조회
     */
    @Transactional(readOnly = true)
    public Page<GetTourResponse> findAllToursByCategory(Authentication authentication,
        List<String> categories, Pageable pageable) {
        Optional<User> user = extractUser(authentication);

        Page<Tour> tours = getToursByCategory(categories, pageable);
        return tours.map(tour -> TourDtoConverter.tourToGetTourResponse(tour,
            user.map(u -> wishlistRepository.findByUserAndTour(u, tour).isPresent())
                .orElse(false)));
    }


    private Page<Tour> getToursByCategory(List<String> categories, Pageable pageable) {
        if (categories == null || categories.isEmpty()) {
            return tourRepository.findAllByStatus(Status.ACTIVE, pageable);
        } else {
            List<Category> categoryList = categoryRepository.findByNameIn(categories);
            return tourRepository.findByCategoriesInAndStatus(categoryList, Status.ACTIVE,
                pageable);
        }
    }

    private Optional<User> extractUser(Authentication authentication) {
        if (authentication != null
            && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userRepository.findById(userDetails.getId());
        }
        return Optional.empty();
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
        for (PostPlaceRequest postPlaceRequest : postTourRequest.getPlaces()) {
            postPlaceRequest.setPlaceOrder(order++);
            Place place = placeService.createPlace(postPlaceRequest);
            place.setTour(tour);
            places.add(place);
        }
        tour.setPlaces(places);

        // 카테고리 정보 설정
        if (postTourRequest.getCategoryNames() != null && !postTourRequest.getCategoryNames()
            .isEmpty()) {
            List<Category> categories = categoryRepository.findByNameIn(
                postTourRequest.getCategoryNames());
            categories.forEach(category -> {
                if (tour.getTourCategories().stream()
                    .noneMatch(tc -> tc.getCategory().equals(category))) {
                    tour.getTourCategories().add(new TourCategory(tour, category));
                }
            });
        }
        // 유저가 투어를 한번이라도 생성하면 hasCreatedTour 필드 true로 설정
        user.setHasCreatedTour(true);
        userRepository.save(user);
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
        if (putTourRequest.getDeletedPlaceIds() != null && !putTourRequest.getDeletedPlaceIds()
            .isEmpty()) {
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
                .filter(p -> p.getId() != null && p.getId().equals(placeInfoDto.getPlaceId()))
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
     * 투어 상태 변경
     */
    public void updateTourStatus(Long id, Status status) {
        Tour tour = tourRepository.findById(id)
            .orElseThrow(() -> new TourException(TOUR_NOT_FOUND));
        tour.setStatus(status);
        tourRepository.save(tour);
    }

//    /**
//     * 투어 삭제
//     */
//    public void deleteTour(Long id) {
//        Tour tour = tourRepository.findById(id).orElseThrow();
//        tour.setStatus(Status.DELETED);
//        tourRepository.save(tour);
//    }

}
