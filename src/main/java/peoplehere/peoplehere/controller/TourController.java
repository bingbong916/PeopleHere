package peoplehere.peoplehere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.common.exception.TourException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.tour.*;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.domain.TourDate;
import peoplehere.peoplehere.domain.TourHistory;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.service.TourService;
import peoplehere.peoplehere.util.BindingResultUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;

    @PostMapping("/new")
    public BaseResponse<GetTourResponse> addTour(@Valid @RequestBody PostTourRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = BindingResultUtils.getErrorMessages(bindingResult);
            throw new TourException(INVALID_TOUR_VALUE, errorMessages);
        }
        log.info("Create tour request: {}", request.getName());
        Tour tour = tourService.createTour(request);
        return new BaseResponse<>(TourDtoConverter.tourToGetTourResponse(tour));
    }

    @PutMapping("/{id}")
    public BaseResponse<GetTourResponse> updateTour(@PathVariable Long id, @Valid @RequestBody PutTourRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = BindingResultUtils.getErrorMessages(bindingResult);
            throw new TourException(INVALID_TOUR_VALUE, errorMessages);
        }
        log.info("Update tour request for ID: {}, {}", id, request.getName());
        tourService.modifyTour(id, request);
        return new BaseResponse<>(new GetTourResponse());
    }

    @PatchMapping("/{id}")
    public BaseResponse<Void> deleteTour(@PathVariable Long id) {
        log.info("Delete tour request for ID: {}", id);
        tourService.deleteTour(id);
        return new BaseResponse<>(null);
    }

    @PatchMapping("/{id}/status")
    public BaseResponse<Void> updateTourStatus(@PathVariable Long id, @RequestParam Status status) {
        log.info("Update tour status request for ID: {}, Status: {}", id, status);
        tourService.updateTourStatus(id, status);
        return new BaseResponse<>(null);
    }

    @PostMapping("/{id}/dates")
    public BaseResponse<Void> addOrUpdateTourDate(@PathVariable Long id, @Valid @RequestBody PostTourDateRequest request) {
        tourService.addOrUpdateTourDate(id, request.getDate(), request.getTime());
        return new BaseResponse<>(null);
    }

    @PatchMapping("/dates/{tourDateId}")
    public BaseResponse<Void> deleteTourDate(@PathVariable Long tourDateId) {
        tourService.removeTourDate(tourDateId);
        return new BaseResponse<>(null);
    }


    @GetMapping("/{id}/dates")
    public BaseResponse<List<GetTourDatesResponse>> getTourDates(
            @PathVariable Long id) {
        List<GetTourDatesResponse> responses = tourService.getTourDates(id);
        return new BaseResponse<>(responses);
    }

    @GetMapping("")
    public BaseResponse<Map<String, Object>> getAllTours(
            @RequestParam(required = false) List<String> categories, Pageable pageable) {
        Page<Tour> toursPage;
        if (categories == null || categories.isEmpty()) {
            // 페이징 정보와 랜덤 정렬을 사용하여 모든 투어 조회
            toursPage = tourService.findAllTours(pageable);
        } else {
            // 선택된 카테고리에 따라 투어 조회
            toursPage = tourService.findAllToursByCategory(categories, pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", toursPage.getContent().stream().map(TourDtoConverter::tourToGetTourResponse).collect(Collectors.toList()));
        response.put("currentPage", toursPage.getNumber());
        response.put("totalPages", toursPage.getTotalPages());
        response.put("totalElements", toursPage.getTotalElements());
        response.put("size", toursPage.getSize());

        return new BaseResponse<>(response);
    }

    @GetMapping("/{id}")
    public BaseResponse<GetTourResponse> getTour(@PathVariable Long id) {
        log.info("Get tour request for ID: {}", id);
        Tour findTour = tourService.findTourById(id);
        return new BaseResponse<>(TourDtoConverter.tourToGetTourResponse(findTour));
    }

    @PostMapping("/{tid}/join")
    public BaseResponse<String> joinTour(@PathVariable Long tid, @RequestParam Long uid) {
        TourHistory tourHistory = tourService.joinTour(uid, tid);
        return new BaseResponse<>("User " + uid + " joined tour " + tid);
    }
}