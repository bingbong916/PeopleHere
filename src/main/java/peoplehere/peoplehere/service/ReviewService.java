package peoplehere.peoplehere.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.common.exception.ReviewException;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.controller.dto.review.GetReviewResponse;
import peoplehere.peoplehere.controller.dto.review.PostReviewRequest;
import peoplehere.peoplehere.controller.dto.user.UserInfoDto;
import peoplehere.peoplehere.domain.Review;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.repository.ReviewRepository;
import peoplehere.peoplehere.repository.UserRepository;
import peoplehere.peoplehere.util.security.UserDetailsImpl;

import java.util.List;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰 생성
     */
    public void addReview(Authentication authentication, Long userId, PostReviewRequest request) {
        Long reviewerId = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        if (reviewerId.equals(userId)) {
            throw new ReviewException(SAME_AS_REVIEWEE_USER);
        }

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        User reviewee = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Review review = new Review();
        review.setContent(request.getContent());
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        reviewRepository.save(review);
    }


    /**
     * 특정 유저의 리뷰 반환
     */
    public List<GetReviewResponse> getReviewsForUser(Long userId) {
        List<Review> reviews = reviewRepository.findByRevieweeId(userId);

        if (reviews.isEmpty()) {
            throw new ReviewException(REVIEW_NOT_FOUND);
        }

        return reviews.stream()
                .map(review -> new GetReviewResponse(
                        review.getId(),
                        new UserInfoDto(
                                review.getReviewer().getId(),
                                review.getReviewer().getFirstName(),
                                review.getReviewer().getImageUrl()),
                        review.getContent(),
                        review.getCreatedAt()))
                .toList();
    }

}
