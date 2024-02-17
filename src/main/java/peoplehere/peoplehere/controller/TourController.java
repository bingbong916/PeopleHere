package peoplehere.peoplehere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.common.exception.TourException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.tour.*;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.service.TourService;
import peoplehere.peoplehere.service.UserService;
import peoplehere.peoplehere.util.BindingResultUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import peoplehere.peoplehere.util.security.UserDetailsImpl;

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
        log.info("Create tour request: {}", request.getTourName());
        Tour tour = tourService.createTour(request);
        return new BaseResponse<>(TourDtoConverter.tourToGetTourResponse(tour, false));
    }

    @PutMapping("/{id}")
    public BaseResponse<GetTourResponse> updateTour(@PathVariable Long id, @Valid @RequestBody PutTourRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = BindingResultUtils.getErrorMessages(bindingResult);
            throw new TourException(INVALID_TOUR_VALUE, errorMessages);
        }
        log.info("Update tour request for ID: {}, {}", id, request.getTourName());
        tourService.modifyTour(id, request);
        return new BaseResponse<>(new GetTourResponse());
    }

    // 투어 삭제하는 로직 Status 변경하는 로직과 동일
//    @PatchMapping("/{id}")
//    public BaseResponse<Void> deleteTour(@PathVariable Long id) {
//        log.info("Delete tour request for ID: {}", id);
//        tourService.deleteTour(id);
//        return new BaseResponse<>(null);
//    }

    @PatchMapping("/{id}/status")
    public BaseResponse<Void> updateTourStatus(@PathVariable Long id, @RequestParam Status status) {
        log.info("Update tour status request for ID: {}, Status: {}", id, status);
        tourService.updateTourStatus(id, status);
        return new BaseResponse<>(null);
    }

    @GetMapping("")
    public BaseResponse<Map<String, Object>> getAllTours(Authentication authentication,
            @RequestParam(required = false) List<String> categories, Pageable pageable) {
        Page<GetTourResponse> toursPage = (categories == null || categories.isEmpty()) ?
                tourService.findAllTours(authentication, pageable) :
                tourService.findAllToursByCategory(authentication, categories, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", toursPage.getContent());
        response.put("currentPage", toursPage.getNumber());
        response.put("totalPages", toursPage.getTotalPages());
        response.put("totalElements", toursPage.getTotalElements());
        response.put("size", toursPage.getSize());

        return new BaseResponse<>(response);
    }

    @GetMapping("/{id}")
    public BaseResponse<GetTourResponse> getTour(Authentication authentication, @PathVariable Long id) {
        log.info("Get tour request for ID: {}", id);
        GetTourResponse tourResponse = tourService.findTourById(authentication, id);
        return new BaseResponse<>(tourResponse);
    }

}