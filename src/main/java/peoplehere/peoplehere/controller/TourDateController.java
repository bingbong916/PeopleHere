package peoplehere.peoplehere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.tour.*;
import peoplehere.peoplehere.domain.enums.TourDateStatus;
import peoplehere.peoplehere.domain.enums.TourHistoryStatus;
import peoplehere.peoplehere.service.TourDateService;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tour-dates")
public class TourDateController {

    private final TourDateService tourDateService;

    @PostMapping("/{tourId}/dates")
    public BaseResponse<Void> addOrUpdateTourDate(@PathVariable Long tourId, @Valid @RequestBody PostTourDateRequest request) {
        tourDateService.addOrUpdateTourDate(tourId, request.getDate(), request.getTime());
        return new BaseResponse<>(null);
    }

    @PatchMapping("/{tourDateId}/status")
    public BaseResponse<Void> updateTourDateStatus(@PathVariable Long tourDateId, @RequestParam TourDateStatus status) {
        tourDateService.updateTourDateStatus(tourDateId, status);
        return new BaseResponse<>(null);
    }

    @GetMapping("/{tourId}/dates")
    public BaseResponse<List<GetTourDatesResponse>> getTourDates(
            @PathVariable Long tourId) {
        List<GetTourDatesResponse> responses = tourDateService.getTourDates(tourId);
        return new BaseResponse<>(responses);
    }


    @PostMapping("/{tourDateId}/join")
    public BaseResponse<String> joinTour(@PathVariable Long tourDateId, @RequestParam Long userId) {
        tourDateService.joinTourDate(tourDateId, userId);
        return new BaseResponse<>("User " + userId + " joined tour date " + tourDateId);
    }

    @PatchMapping("/{tourHistoryId}/reservation-status")
    public BaseResponse<Void> updateTourStatus(@PathVariable Long tourHistoryId, @RequestParam TourHistoryStatus status) {
        log.info("Update tour history status request for ID: {}, Status: {}", tourHistoryId, status);
        tourDateService.updateReservationStatus(tourHistoryId, status);
        return new BaseResponse<>(null);
    }
}