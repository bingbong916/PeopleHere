package peoplehere.peoplehere.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.controller.dto.tour.GetTourResponse;
import peoplehere.peoplehere.controller.dto.tour.TourDtoConverter;
import peoplehere.peoplehere.util.security.UserDetailsImpl;
import peoplehere.peoplehere.controller.dto.jwt.JwtTokenResponse;
import peoplehere.peoplehere.controller.dto.user.*;
import peoplehere.peoplehere.domain.*;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.domain.enums.TourHistoryStatus;
import peoplehere.peoplehere.repository.*;
import peoplehere.peoplehere.util.jwt.JwtProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final TourRepository tourRepository;
    private final UserBlockRepository userBlockRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtBlackListRepository jwtBlackListRepository;

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }

    /**
     * 회원 가입
     */
    public User createUser(PostUserRequest postUserRequest) {
        log.info("[UserService.createUser]");

        // 이메일 중복 검사
        validateEmail(postUserRequest.getEmail());

        //패스워드 암호화
        String encodedPassword = passwordEncoder.encode(postUserRequest.getPassword());
        postUserRequest.resetPassword(encodedPassword);

        //DB 저장
        User user = UserDtoConverter.postUserRequestToUser(postUserRequest);
        userRepository.save(user);

        return user;
    }

    private void validateEmail(String email) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new UserException(DUPLICATE_EMAIL);
        }
    }

    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email).isEmpty();
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
     * 로그인
     */
    public JwtTokenResponse login(PostLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        if (user.getStatus() == Status.DELETED) {
            throw new UserException(USER_DELETED);
        }
        validatePassword(request.getPassword(),user.getPassword());
        String accessToken = jwtProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());
        return new JwtTokenResponse("Bearer", accessToken, refreshToken);
    }

    private void validatePassword(String password, String encodedPassword) {
        log.info("password: " + password);
        log.info("encodedPassword: " + encodedPassword);
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new UserException(PASSWORD_NO_MATCH);
        }
    }

    /**
     * 로그아웃
     */
    public void logout(String token) {
        JwtBlackList blackList = jwtBlackListRepository.findByToken(token);
        if (blackList == null) {
            jwtBlackListRepository.save(new JwtBlackList(token));
        }
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
    public GetUserResponse getUser(Long userId) {
        User user = getUserOrThrow(userId);
        return UserDtoConverter.userToGetUserResponse(user);
    }

    /**
     * 화원 정보 수정
     * TODO: userModifyRequest 형식 만들기
     */
    public void modifyUser(Long userId, PostModifyRequest modifyRequest) {
        User user = getUserOrThrow(userId);

        // 필수 필드 업데이트
        if (modifyRequest.getEmail() != null && !modifyRequest.getEmail().isEmpty()) {
            user.setEmail(modifyRequest.getEmail());
        }
        if (modifyRequest.getPassword() != null && !modifyRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(modifyRequest.getPassword()));
        }
        if (modifyRequest.getName() != null && !modifyRequest.getName().isEmpty()) {
            user.setName(modifyRequest.getName());
        }

        // 선택적 필드 업데이트
        if (modifyRequest.getGender() != null) {
            user.setGender(modifyRequest.getGender());
        }
        if (modifyRequest.getAddress() != null) {
            user.setAddress(modifyRequest.getAddress());
        }
        if (modifyRequest.getBirth() != null) {
            user.setBirth(modifyRequest.getBirth());
        }
        if (modifyRequest.getJob() != null) {
            user.setJob(modifyRequest.getJob());
        }
        if (modifyRequest.getAlmaMater() != null) {
            user.setAlmaMater(modifyRequest.getAlmaMater());
        }
        if (modifyRequest.getHobby() != null) {
            user.setHobby(modifyRequest.getHobby());
        }
        if (modifyRequest.getPet() != null) {
            user.setPet(modifyRequest.getPet());
        }
        if (modifyRequest.getFavourite() != null) {
            user.setFavourite(modifyRequest.getFavourite());
        }
        if (modifyRequest.getImageUrl() != null) {
            user.setImageUrl(modifyRequest.getImageUrl());
        }
        if (modifyRequest.getContent() != null) {
            user.setContent(modifyRequest.getContent());
        }

        userRepository.save(user);
    }


    /**
     * 유저가 만든 투어 조회
     */
    @Transactional(readOnly = true)
    public List<GetTourResponse> getUserTours(Long userId, String option, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UserException(USER_NOT_LOGGED_IN);
        }

        User user = getUserOrThrow(userId);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        List<GetTourResponse> responses = new ArrayList<>();

        // 만든 투어 조회
        if (option == null || "created".equals(option)) {
            List<Tour> createdTours = new ArrayList<>(user.getTours());
            createdTours.forEach(tour -> {
                boolean isWished = wishlistRepository.findByUserAndTour(currentUser, tour).isPresent();
                responses.add(TourDtoConverter.tourToGetTourResponse(tour, isWished));
            });
        }

        // 참여한 투어 조회
        if (option == null || "attended".equals(option)) {
            user.getTourHistories().stream()
                    .filter(th -> th.getStatus().equals(TourHistoryStatus.CONFIRMED))
                    .forEach(th -> {
                        boolean isWished = wishlistRepository.findByUserAndTour(currentUser, th.getTour()).isPresent();
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
