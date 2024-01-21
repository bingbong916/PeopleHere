package peoplehere.peoplehere.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.common.exception.PlaceException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.place.*;
import peoplehere.peoplehere.service.PlaceService;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping("/new")
    public BaseResponse<PostPlaceResponse> addPlace(@RequestBody PostPlaceRequest request) {
        log.info("Create place request: {}", request.getAddress());
        // TODO: 장소 생성 로직 구현 예정
        return new BaseResponse<>(new PostPlaceResponse(1L, request.getContent(), request.getAddress(), request.getOrder()));
    }

    @PutMapping("/{id}")
    public BaseResponse<PutPlaceResponse> updatePlace(@PathVariable Long id, @RequestBody PutPlaceRequest request) {
        log.info("Update place request for ID: {}, {}", id, request.getAddress());
        // TODO: 장소 수정 로직 구현 예정
        return new BaseResponse<>(new PutPlaceResponse(id, request.getContent(), request.getAddress(), request.getOrder()));
    }

    @PatchMapping("/{id}")
    public BaseResponse<Void> deletePlace(@PathVariable Long id) {
        log.info("Delete place request for ID: {}", id);
        // TODO: 장소 삭제 로직 구현 예정
        return new BaseResponse<>(null);
    }

    @GetMapping("")
    public BaseResponse<GetPlacesResponse> getAllPlaces() {
        log.info("Get all places request");
        // TODO: 모든 장소 조회 로직 구현 예정
        return new BaseResponse<>(new GetPlacesResponse());
    }

    @GetMapping("/{id}")
    public BaseResponse<GetPlaceResponse> getPlace(@PathVariable Long id) {
        log.info("Get place request for ID: {}", id);
        // TODO: 특정 장소 조회 로직 구현 예정
        throw new PlaceException(PLACE_NOT_FOUND, "Place ID: " + id + " not found");
    }
}
