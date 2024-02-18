package peoplehere.peoplehere.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.review.GetReviewResponse;
import peoplehere.peoplehere.controller.dto.review.PostReviewRequest;
import peoplehere.peoplehere.service.ReviewService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;


    @PostMapping("/{userId}")
    public BaseResponse<Void> addReview(Authentication authentication, @PathVariable Long userId, @Valid @RequestBody PostReviewRequest request) {
        reviewService.addReview(authentication, userId, request);
        return new BaseResponse<>(null);
    }

    @GetMapping("/user/{userId}")
    public BaseResponse<List<GetReviewResponse>> getReviewsForUser(@PathVariable Long userId) {
        List<GetReviewResponse> responses = reviewService.getReviewsForUser(userId);
        return new BaseResponse<>(responses);
    }

//    @PutMapping("/{id}")
//    public BaseResponse<GetReviewResponse> updateReview(@PathVariable Long id, @Valid @RequestBody PutReviewRequest request, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            throw new ReviewException(INVALID_REVIEW_VALUE);
//        }
//        log.info("Updating review with ID {}: {}", id, request);
//        // TODO: 리뷰 수정 로직 구현 예정
//        return new BaseResponse<>(new GetReviewResponse(id, 1L, request.getContent());
//    }

//    @PatchMapping("/{id}")
//    public BaseResponse<Void> deleteReview(@PathVariable Long id) {
//        log.info("Deleting review with ID {}", id);
//        // TODO: 리뷰 삭제 로직 구현 예정
//        return new BaseResponse<>(null);
//    }
}
