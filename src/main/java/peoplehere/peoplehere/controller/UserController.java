package peoplehere.peoplehere.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.tour.GetTourResponse;
import peoplehere.peoplehere.controller.dto.user.*;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.service.UserService;
import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.INVALID_USER_VALUE;
import static peoplehere.peoplehere.util.BindingResultUtils.getErrorMessages;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public BaseResponse<PostUserResponse> signUp(@Validated @RequestBody PostUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        log.info("User sign-up request: {}", request.getEmail());
        User user = userService.createUser(request);
        return new BaseResponse<>(new PostUserResponse(user.getId()));
    }

    @PatchMapping("/{id}")
    public BaseResponse<Void> deactivateUser(@PathVariable Long id) {
        log.info("Deactivate user request for ID: {}", id);
        userService.deactivateUser(id);
        return new BaseResponse<>(null);
    }

    @PostMapping("/login")
    public BaseResponse<PostLoginResponse> login(@Validated @RequestBody PostLoginRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        log.info("User login request: {}", request.getEmail());
        String jwt = userService.login(request);
        return new BaseResponse<>(new PostLoginResponse(jwt));
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestBody PostLogoutRequest request) {
        log.info("User logout request: {}", request.getEmail());
        // TODO: 추후에 인증 방식을 변경하면 수정 필요.
        // TODO: 로그아웃 로직 구현 예정
        return new BaseResponse<>(null);
    }

    @GetMapping("/{id}")
    public BaseResponse<GetUserResponse> getUserInfo(@PathVariable Long id) {
        log.info("Get user info for ID: {}", id);
        // TODO: 유저 정보 조회 로직 구현 예정
        return new BaseResponse<>(new GetUserResponse());
    }

    @GetMapping("/{id}/tours")
    public BaseResponse<GetTourResponse> getUserTours(@PathVariable Long id, @RequestParam String option) {
        log.info("Get tours for user ID: {}, Option: {}", id, option); // option값: created, attended
        // TODO: 유저가 만든 또는 이용한 투어 조회 로직 구현 예정
        return new BaseResponse<>(new GetTourResponse());
    }

    @GetMapping("/{id}/chats")
    public BaseResponse<GetUserChatsResponse> getUserChats(@PathVariable Long id, @RequestParam String option) {
        log.info("Get chats for user ID: {}, Option: {}", id, option); // option값: traveler, leader
        // TODO: 유저 채팅 조회 로직 구현 예정
        return new BaseResponse<>(new GetUserChatsResponse());
    }

    @PostMapping("/block")
    public BaseResponse<Void> blockUser(@RequestParam Long targetId) {
        log.info("Block user request for target ID: {}", targetId);
        // TODO: 차단하기 로직 구현 예정
        return new BaseResponse<>(null);
    }
}
