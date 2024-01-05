package peoplehere.peoplehere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.common.exception.TourException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.tour.GetTourResponse;
import peoplehere.peoplehere.controller.dto.tour.PostTourRequest;
import peoplehere.peoplehere.controller.dto.tour.PutTourRequest;
import peoplehere.peoplehere.controller.dto.tour.TourDtoConverter;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.service.TourService;
import peoplehere.peoplehere.util.BindingResultUtils;

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
        return new BaseResponse<>(TourDtoConverter.TourToGetTourResponse(tour));
    }

    @PutMapping("/{id}")
    public BaseResponse<GetTourResponse> updateTour(@PathVariable Long id, @Valid @RequestBody PutTourRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = BindingResultUtils.getErrorMessages(bindingResult);
            throw new TourException(INVALID_TOUR_VALUE, errorMessages);
        }
        log.info("Update tour request for ID: {}, {}", id, request.getName());
        // TODO: 투어 수정 로직 구현 예정
        return new BaseResponse<>(new GetTourResponse());
    }

    @PatchMapping("/{id}")
    public BaseResponse<Void> deleteTour(@PathVariable Long id) {
        log.info("Delete tour request for ID: {}", id);
        // TODO: 투어 삭제 로직 구현 예정
        return new BaseResponse<>(null);
    }

    @GetMapping("")
    public BaseResponse<GetTourResponse> getAllTours(@RequestParam(required = false) String language, @RequestParam(required = false) String category) {
        log.info("Get all tours request with Language: {}, Category: {}", language, category);
        // TODO: 모든 투어 조회 로직 구현 예정
        return new BaseResponse<>(new GetTourResponse());
    }

    @GetMapping("/{id}")
    public BaseResponse<GetTourResponse> getTour(@PathVariable Long id) {
        log.info("Get tour request for ID: {}", id);
        // TODO: 특정 투어 조회 로직 구현 예정
        throw new TourException(TOUR_NOT_FOUND, "Tour ID: " + id + " not found");
    }
}