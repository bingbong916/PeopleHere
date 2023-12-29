package peoplehere.peoplehere.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import peoplehere.peoplehere.common.exception.ReviewException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.review.GetReviewResponse;
import peoplehere.peoplehere.controller.dto.review.PostReviewRequest;
import peoplehere.peoplehere.controller.dto.review.PutReviewRequest;
import peoplehere.peoplehere.util.BindingResultUtils;
import peoplehere.peoplehere.service.ReviewService;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/new")
    public BaseResponse<GetReviewResponse> addReview(@Valid @RequestBody PostReviewRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = BindingResultUtils.getErrorMessages(bindingResult);
            throw new ReviewException(INVALID_REVIEW_VALUE, errorMessages);
        }
        log.info("Adding new review: {}", request);
        // TODO: 리뷰 추가 로직 구현 예정
        return new BaseResponse<>(new GetReviewResponse(1L, request.getUserId(), request.getTourId(), request.getContent(), request.getScore()));
    }

    @PutMapping("/{id}")
    public BaseResponse<GetReviewResponse> updateReview(@PathVariable Long id, @Valid @RequestBody PutReviewRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = BindingResultUtils.getErrorMessages(bindingResult);
            throw new ReviewException(INVALID_REVIEW_VALUE, errorMessages);
        }
        log.info("Updating review with ID {}: {}", id, request);
        // TODO: 리뷰 수정 로직 구현 예정
        return new BaseResponse<>(new GetReviewResponse(id, 1L, 1L, request.getContent(), request.getScore()));
    }

    @PatchMapping("/{id}")
    public BaseResponse<Void> deleteReview(@PathVariable Long id) {
        log.info("Deleting review with ID {}", id);
        // TODO: 리뷰 삭제 로직 구현 예정
        return new BaseResponse<>(null);
    }

    @GetMapping("/{id}")
    public BaseResponse<GetReviewResponse> getReview(@PathVariable Long id) {
        log.info("Fetching review with ID {}", id);
        // TODO: 리뷰 조회 로직 구현 예정
        throw new ReviewException(REVIEW_NOT_FOUND, "Review ID: " + id + " not found");
    }
}
