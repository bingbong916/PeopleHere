package peoplehere.peoplehere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
    public BaseResponse<Void> addOrUpdateTourDate(@PathVariable Long tourId,
        @Valid @RequestBody PostTourDateRequest request) {
        tourDateService.addOrUpdateTourDate(tourId, request.getDate(), request.getTime());
        return new BaseResponse<>(null);
    }

    @PatchMapping("/{tourDateId}/status")
    public BaseResponse<Void> updateTourDateStatus(@PathVariable Long tourDateId,
        @RequestParam TourDateStatus status) {
        tourDateService.updateTourDateStatus(tourDateId, status);
        return new BaseResponse<>(null);
    }

    @GetMapping("/{tourId}/dates")
    public BaseResponse<List<GetTourDatesResponse>> getAllTourDates(
        @PathVariable Long tourId) {
        List<GetTourDatesResponse> responses = tourDateService.getTourDates(tourId);
        return new BaseResponse<>(responses);
    }

    @GetMapping("/{tourDateId}/date")
    public BaseResponse<TourDateInfoDto> getTourDate(@PathVariable Long tourDateId) {
        TourDateInfoDto response = tourDateService.getTourDateInfo(tourDateId);
        return new BaseResponse<>(response);
    }


    @PostMapping("/{tourDateId}/join")
    public BaseResponse<String> joinTour(Authentication authentication,
        @PathVariable Long tourDateId) {
        tourDateService.joinTourDate(authentication, tourDateId);
        return new BaseResponse<>("Current user joined tour date " + tourDateId);
    }

    @PatchMapping("/{tourHistoryId}/reservation-status")
    public BaseResponse<Void> updateTourStatus(@PathVariable Long tourHistoryId,
        @RequestParam TourHistoryStatus status) {
        log.info("Update tour history status request for ID: {}, Status: {}", tourHistoryId,
            status);
        tourDateService.updateReservationStatus(tourHistoryId, status);
        return new BaseResponse<>(null);
    }

    /**
     * 한 투어에 대한 참여 요청들 조회
     */
    @GetMapping("/participants/{tourId}")
    public BaseResponse<List<GetTourParticipantsResponse>> getParticipantRequests(@PathVariable Long tourId) {
        List<GetTourParticipantsResponse> participantRequests = tourDateService.getParticipantRequests(
            tourId);
        return new BaseResponse<>(participantRequests);
    }
}