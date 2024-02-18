package peoplehere.peoplehere.service.user;

import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.controller.dto.image.PostImageRequest;
import peoplehere.peoplehere.controller.dto.tour.GetTourResponse;
import peoplehere.peoplehere.controller.dto.tour.TourDtoConverter;
import peoplehere.peoplehere.service.S3Service;
import peoplehere.peoplehere.util.security.UserDetailsImpl;
import peoplehere.peoplehere.controller.dto.user.*;
import peoplehere.peoplehere.domain.*;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.domain.enums.TourHistoryStatus;
import peoplehere.peoplehere.repository.*;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    //    protected final SearchHistoryRepository searchHistoryRepository;
    private final TourRepository tourRepository;
    private final UserBlockRepository userBlockRepository;
    private final UserLanguageRepository userLanguageRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final LanguageRepository languageRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final QuestionRepository questionRepository;

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    private String saveImage(PostImageRequest postImageRequest) {
        byte[] decodingImage = Base64.getDecoder().decode(postImageRequest.getEncodingString());
        String storedFileName = s3Service.saveByteArrayToS3(decodingImage,
            postImageRequest.getOriginalFileName()); //S3에 파일 저장
        return s3Service.getPictureS3Url(storedFileName);
    }

    /**
     * 회원 탈퇴
     */
    public void updateUserStatus(Long id, Status status) {
        User user = getUserOrThrow(id);
        user.setStatus(status);
        userRepository.save(user);
    }


    /**
     * 비밀번호 재설정
     */
    public void updatePassword(Authentication authentication, String newPassword) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new UserException(SAME_AS_OLD_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * 모든 유저 조회
     */
    public List<GetUserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<GetUserResponse> getUserResponses = new ArrayList<>();
        for (User user : users) {
            getUserResponses.add(UserDtoConverter.userToGetUserResponse(user));
        }
        return getUserResponses;
    }

    /**
     * 특정 유저 조회
     */
    public User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    /**
     * 화원 정보 수정
     * TODO: userModifyRequest 형식 만들기
     */
    public void modifyUser(Authentication authentication, PostModifyRequest modifyRequest) {
        // TODO: 민감한 유저 정보 업데이트 분리 구현
//        if (modifyRequest.getEmail() != null && !modifyRequest.getEmail().isEmpty()) {
//            user.setEmail(modifyRequest.getEmail());
//        }
//        if (modifyRequest.getPassword() != null && !modifyRequest.getPassword().isEmpty()) {
//            user.setPassword(passwordEncoder.encode(modifyRequest.getPassword()));
//        }
//        if (modifyRequest.getName() != null && !modifyRequest.getName().isEmpty()) {
//            user.setName(modifyRequest.getName());
//        }
//
//        if (modifyRequest.getGender() != null) {
//            user.setGender(modifyRequest.getGender());
//        }
//        if (modifyRequest.getBirth() != null) {
//            user.setBirth(modifyRequest.getBirth());
//        }

        User user = getAuthenticatedUser(authentication);

        if (modifyRequest.getLanguages() != null) {
            Set<Long> newLanguageIds = new HashSet<>(modifyRequest.getLanguages());
            user.getLanguages().removeIf(
                userLanguage -> !newLanguageIds.contains(userLanguage.getLanguage().getId()));
            newLanguageIds.forEach(languageId -> {
                if (user.getLanguages().stream()
                    .noneMatch(ul -> ul.getLanguage().getId().equals(languageId))) {
                    Language language = languageRepository.findById(languageId)
                        .orElseThrow(() -> new UserException(LANGUAGE_NOT_FOUND));
                    UserLanguage userLanguage = new UserLanguage();
                    userLanguage.setUser(user);
                    userLanguage.setLanguage(language);
                    userLanguageRepository.save(userLanguage);
                }
            });
        }

        if (modifyRequest.getQuestions() != null) {
            modifyRequest.getQuestions().forEach((questionId, answer) -> {
                Optional<UserQuestion> existingUserQuestion = user.getUserQuestions().stream()
                    .filter(uq -> uq.getQuestion().getId().equals(questionId))
                    .findFirst();

                if (existingUserQuestion.isPresent()) {
                    existingUserQuestion.get().setAnswer(answer);
                } else {
                    Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new UserException(QUESTION_NOT_FOUND));
                    UserQuestion newUserQuestion = new UserQuestion();
                    newUserQuestion.setUser(user);
                    newUserQuestion.setQuestion(question);
                    newUserQuestion.setAnswer(answer);
                    userQuestionRepository.save(newUserQuestion);
                }
            });
        }

        Optional.ofNullable(modifyRequest.getAddress()).ifPresent(user::setAddress);
        Optional.ofNullable(modifyRequest.getJob()).ifPresent(user::setJob);
        Optional.ofNullable(modifyRequest.getAlmaMater()).ifPresent(user::setAlmaMater);
        Optional.ofNullable(modifyRequest.getHobby()).ifPresent(user::setHobby);
        Optional.ofNullable(modifyRequest.getPet()).ifPresent(user::setPet);
        Optional.ofNullable(modifyRequest.getFavourite()).ifPresent(user::setFavourite);
        Optional.ofNullable(modifyRequest.getContent()).ifPresent(user::setContent);

        if (modifyRequest.getImageRequest() != null) {
            String imageUrl = saveImage(modifyRequest.getImageRequest());
            user.setImageUrl(imageUrl);
        }

        userRepository.save(user);
    }


    /**
     * 유저가 만든 투어 조회
     */
    @Transactional(readOnly = true)
    public List<GetTourResponse> getUserTours(Long userId, String option,
        Authentication authentication) {
        User user = getUserOrThrow(userId);

        User currentUser;

        if (authentication != null && authentication.getPrincipal() != null) {
            currentUser = getAuthenticatedUser(authentication);
        } else {
            currentUser = null;
        }

        List<GetTourResponse> responses = new ArrayList<>();

        // 만든 투어 조회
        if (option == null || "created".equals(option)) {
            List<Tour> createdTours = new ArrayList<>(user.getTours());
            createdTours.forEach(tour -> {
                boolean isWished = false;
                if (currentUser != null) {
                    isWished = wishlistRepository.findByUserAndTour(currentUser, tour).isPresent();
                }
                responses.add(TourDtoConverter.tourToGetTourResponse(tour, isWished));
            });
        }

        // 참여한 투어 조회
        if (option == null || "attended".equals(option)) {
            user.getTourHistories().stream()
                .filter(th -> th.getStatus().equals(TourHistoryStatus.CONFIRMED))
                .forEach(th -> {
                    boolean isWished = false;
                    if (currentUser != null) {
                        isWished = wishlistRepository.findByUserAndTour(currentUser, th.getTour())
                            .isPresent();
                    }
                    responses.add(TourDtoConverter.tourToGetTourResponse(th.getTour(), isWished));
                });
        }

        return responses;
    }


    /**
     * 위시리스트 추가 및 삭제
     */
    public void toggleWishlist(Authentication authentication, Long tourId) {

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UserException(USER_NOT_LOGGED_IN);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        Tour tour = tourRepository.findById(tourId)
            .orElseThrow(() -> new UserException(TOUR_NOT_FOUND));

        Optional<Wishlist> wishlistOpt = wishlistRepository.findByUserAndTour(user, tour);
        if (wishlistOpt.isPresent()) {
            wishlistRepository.delete(wishlistOpt.get());
        } else {
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUser(user);
            newWishlist.setTour(tour);
            wishlistRepository.save(newWishlist);
        }
    }

    /**
     * 유저 위시리스트 조회
     */
    @Transactional(readOnly = true)
    public List<GetTourResponse> getUserWishlist(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);

        List<Wishlist> wishlists = wishlistRepository.findByUser(user);
        List<GetTourResponse> responses = new ArrayList<>();
        for (Wishlist wishlist : wishlists) {
            Tour tour = wishlist.getTour();
            responses.add(TourDtoConverter.tourToGetTourResponse(tour, true));
        }

        return responses;
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UserException(USER_NOT_LOGGED_IN);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    /**
     * 검색 내역 저장
     */
//    private

    /**
     * 유저 채팅 조회
     */


    /**
     * 차단하기
     */
    public void blockUser(Long blockerId, Long blockedId) {
        User blocker = getUserOrThrow(blockerId);
        User blocked = getUserOrThrow(blockedId);

        UserBlock userBlock = new UserBlock();
        userBlock.setBlocker(blocker);
        userBlock.setBlocked(blocked);
        userBlock.setStatus("차단"); // TODO : Status 상수화

        userBlockRepository.save(userBlock);
    }
}
