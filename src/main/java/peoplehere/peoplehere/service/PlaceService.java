package peoplehere.peoplehere.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import peoplehere.peoplehere.controller.dto.place.PlaceDtoConverter;
import peoplehere.peoplehere.controller.dto.place.PostPlaceRequest;
import peoplehere.peoplehere.controller.dto.place.PostPlaceResponse;
import peoplehere.peoplehere.domain.Place;
import peoplehere.peoplehere.repository.PlaceRepository;
import peoplehere.peoplehere.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {

    private final S3Service s3Service;
    private final PlaceRepository placeRepository;

    /**
     * 이미지를 S3에 저장 후 반환 된 url을 Place에 담아줌
     *
     */
    public void saveImages(Place place, List<MultipartFile> images) {
        for (MultipartFile image : images) {
            String storedName = s3Service.saveMultipartFileToS3(image); //S3에 파일 저장
            place.addImageUrls(s3Service.getPictureS3Url(storedName));  //DB에는 파일 URL만 저장
        }
    }

    public Place createPlace(PostPlaceRequest postPlaceRequest, List<MultipartFile> images) {
        Place place = PlaceDtoConverter.postPlaceRequestToPlace(postPlaceRequest);
        saveImages(place, images);
        placeRepository.save(place);
        return place;
    }
}
