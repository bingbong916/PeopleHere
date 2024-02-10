package peoplehere.peoplehere.service;

import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.controller.dto.image.PostImageRequest;
import peoplehere.peoplehere.controller.dto.jwt.JwtTokenResponse;
import peoplehere.peoplehere.controller.dto.user.*;
import peoplehere.peoplehere.domain.*;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.repository.JwtBlackListRepository;
import peoplehere.peoplehere.repository.UserBlockRepository;
import peoplehere.peoplehere.repository.UserRepository;
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
    private final UserBlockRepository userBlockRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtBlackListRepository jwtBlackListRepository;
    private final S3Service s3Service;

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
        user.setImageUrl(saveImage(postUserRequest.getPostImageRequest()));
        userRepository.save(user);

        return user;
    }

    private String saveImage(PostImageRequest postImageRequest) {
        byte[] decodingImage = Base64.getDecoder().decode(postImageRequest.getEncodingString());
        String storedFileName = s3Service.saveByteArrayToS3(decodingImage, postImageRequest.getOriginalFileName()); //S3에 파일 저장
        return s3Service.getPictureS3Url(storedFileName);
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
        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());
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
        if (modifyRequest.getImageRequest() != null) {
            user.setImageUrl(saveImage(modifyRequest.getImageRequest()));
        }
        if (modifyRequest.getContent() != null) {
            user.setContent(modifyRequest.getContent());
        }

        userRepository.save(user);
    }


    /**
     * 유저가 만든 투어 조회
     */
    public List<Tour> getCreatedTour(Long userId) {
        User user = getUserOrThrow(userId);

        return user.getTours();
    }

    /**
     * 유저가 이용한 투어 조회
     */
    public List<TourHistory> getTourHistory(Long userId) {
        User user = getUserOrThrow(userId);
        // TODO: RESERVED는 반환 X, CONFIRMED만 반환시켜야 함
        return user.getTourHistories();
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
