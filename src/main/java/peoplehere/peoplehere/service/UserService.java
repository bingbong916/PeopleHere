package peoplehere.peoplehere.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.controller.dto.user.GetUserResponse;
import peoplehere.peoplehere.controller.dto.user.PostLoginRequest;
import peoplehere.peoplehere.controller.dto.user.PostUserRequest;
import peoplehere.peoplehere.controller.dto.user.UserDtoConverter;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.domain.TourHistory;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원 가입
     */
    public User createUser(PostUserRequest postUserRequest) {
        User user = UserDtoConverter.postUserRequestToUser(postUserRequest);
        userRepository.save(user);
        return user;
    }

    /**
     * 회원 탈퇴
     */
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus("삭제"); //TODO: userStatus 코드 상수화 시키기
    }

    /**
     * 로그인
     */
    public void login(PostLoginRequest postLoginRequest) {
        User findUser = userRepository.findByEmail(postLoginRequest.getEmail());
        if (findUser.getPassword() == postLoginRequest.getPassword()) {

        }


    }

    /**
     * 로그아웃
     */


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
