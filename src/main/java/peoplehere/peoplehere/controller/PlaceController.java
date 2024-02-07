package peoplehere.peoplehere.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import peoplehere.peoplehere.common.exception.PlaceException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.place.*;
import peoplehere.peoplehere.domain.Place;
import peoplehere.peoplehere.service.PlaceService;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    /**
     * @Param  List<PostPlaceRequest>
     * @return List<PostPlaceResponse>
     * 투어 내부에 있는 여러 장소를 DB에 저장한 후 반환
     * PostPlaceRequest에 담긴 MultipartFile을 S3에 저장 후 url로 변경하여 리턴.
     */
    @PostMapping("/new")
    public BaseResponse<List<PostPlaceResponse>> addPlaces(@RequestPart("images") List<MultipartFile> images,
        @RequestPart List<PostPlaceRequest> postPlaceRequests) {
        log.info("Create place request");
        List<Place> places = new ArrayList<>();
        int order = 1;
        for (PostPlaceRequest postPlaceRequest : postPlaceRequests) {
            postPlaceRequest.setOrder(order++);
            places.add(placeService.createPlace(postPlaceRequest, images));
        }
        List<PostPlaceResponse> postPlaceResponses = new ArrayList<>();
        for (Place place : places) {
            postPlaceResponses.add(PlaceDtoConverter.placeToPostPlaceResponse(place));
        }
        return new BaseResponse<>(postPlaceResponses);
    }

//    @PutMapping("/{id}")
//    public BaseResponse<PutPlaceResponse> updatePlace(@PathVariable Long id, @RequestBody PutPlaceRequest request) {
//        log.info("Update place request for ID: {}, {}", id, request.getAddress());
//        // TODO: 장소 수정 로직 구현 예정
//        return new BaseResponse<>(new PutPlaceResponse(id, request.getContent(), request.getAddress(), request.getOrder()));
//    }
//
//    @PatchMapping("/{id}")
//    public BaseResponse<Void> deletePlace(@PathVariable Long id) {
//        log.info("Delete place request for ID: {}", id);
//        // TODO: 장소 삭제 로직 구현 예정
//        return new BaseResponse<>(null);
//    }

//    @GetMapping("")
//    public BaseResponse<GetPlacesResponse> getAllPlaces() {
//        log.info("Get all places request");
//        // TODO: 모든 장소 조회 로직 구현 예정
//        return new BaseResponse<>(new GetPlacesResponse());
//    }

    @GetMapping("/{id}")
    public BaseResponse<GetPlaceResponse> getPlace(@PathVariable Long id) {
        log.info("Get place request for ID: {}", id);
        // TODO: 특정 장소 조회 로직 구현 예정
        throw new PlaceException(PLACE_NOT_FOUND, "Place ID: " + id + " not found");
    }
}
