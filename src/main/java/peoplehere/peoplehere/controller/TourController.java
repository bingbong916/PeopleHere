package peoplehere.peoplehere.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.common.exception.TourException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.tour.GetToursResponse;
import peoplehere.peoplehere.controller.dto.tour.PostTourRequest;
import peoplehere.peoplehere.controller.dto.tour.PutTourRequest;
import peoplehere.peoplehere.service.TourService;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;

    @PostMapping("/new")
    public BaseResponse<GetToursResponse> addTour(@RequestBody PostTourRequest request) {
        log.info("Create tour request: {}", request.getName());
        // TODO: 투어 생성 로직 구현 예정
        return new BaseResponse<>(new GetToursResponse());
    }

    @PutMapping("/{id}")
    public BaseResponse<GetToursResponse> updateTour(@PathVariable Long id, @RequestBody PutTourRequest request) {
        log.info("Update tour request for ID: {}, {}", id, request.getName());
        // TODO: 투어 수정 로직 구현 예정
        return new BaseResponse<>(new GetToursResponse());
    }

    @PatchMapping("/{id}")
    public BaseResponse<Void> deleteTour(@PathVariable Long id) {
        log.info("Delete tour request for ID: {}", id);
        // TODO: 투어 삭제 로직 구현 예정
        return new BaseResponse<>(null);
    }

    @GetMapping("")
    public BaseResponse<GetToursResponse> getAllTours(@RequestParam(required = false) String language, @RequestParam(required = false) String category) {
        log.info("Get all tours request with Language: {}, Category: {}", language, category);
        // TODO: 모든 투어 조회 로직 구현 예정
        return new BaseResponse<>(new GetToursResponse());
    }

    @GetMapping("/{id}")
    public BaseResponse<GetToursResponse> getTour(@PathVariable Long id) {
        log.info("Get tour request for ID: {}", id);
        // TODO: 특정 투어 조회 로직 구현 예정
        throw new TourException(TOUR_NOT_FOUND, "Tour ID: " + id + " not found");
    }
}
