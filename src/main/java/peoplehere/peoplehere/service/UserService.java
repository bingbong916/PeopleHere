package peoplehere.peoplehere.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.controller.dto.jwt.JwtTokenResponse;
import peoplehere.peoplehere.controller.dto.user.GetUserResponse;
import peoplehere.peoplehere.controller.dto.user.PostLoginRequest;
import peoplehere.peoplehere.controller.dto.user.PostUserRequest;
import peoplehere.peoplehere.controller.dto.user.UserDtoConverter;
import peoplehere.peoplehere.domain.JwtBlackList;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.domain.TourHistory;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.repository.JwtBlackListRepository;
import peoplehere.peoplehere.repository.UserRepository;
import peoplehere.peoplehere.util.jwt.JwtProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.DUPLICATE_EMAIL;
import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.PASSWORD_NO_MATCH;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtBlackListRepository jwtBlackListRepository;

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

    /**
     * 회원 탈퇴
     */
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus("삭제"); //TODO: userStatus 코드 상수화 시키기
    }

    /**
     * 로그인
     */
    public JwtTokenResponse login(PostLoginRequest postLoginRequest) {
        User findUser = userRepository.findByEmail(postLoginRequest.getEmail()).orElseThrow();
        validatePassword(postLoginRequest.getPassword(),findUser.getPassword());
        String accessToken = jwtProvider.createAccessToken(findUser.getId());
        String refreshToken = jwtProvider.createRefreshToken(findUser.getId());
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
        User user = userRepository.findById(userId).orElseThrow();
        return UserDtoConverter.userToGetUserResponse(user);
    }

    /**
     * 화원 정보 수정
     * TODO: userModifyRequest 형식 만들기
     */
    public void modifyUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

    }

    /**
     * 유저가 만든 투어 조회
     */
    public List<Tour> getCreatedTour(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getTours();
    }

    /**
     * 유저가 이용한 투어 조회
     */
    public List<TourHistory> getTourHistory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getTourHistories();
    }


    /**
     * 유저 채팅 조회
     */


    /**
     * 차단하기
     */


}
