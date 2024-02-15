package peoplehere.peoplehere.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.jwt.JwtTokenResponse;
import peoplehere.peoplehere.controller.dto.tour.GetTourResponse;
import peoplehere.peoplehere.controller.dto.tour.TourDtoConverter;
import peoplehere.peoplehere.controller.dto.user.*;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.domain.TourHistory;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.repository.UserRepository;
import peoplehere.peoplehere.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;
import static peoplehere.peoplehere.util.BindingResultUtils.getErrorMessages;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;


    @GetMapping("/check-email")
    public BaseResponse<GetEmailCheckResponse> checkEmail(@RequestParam String email) {
        boolean isEmailAvailable = userService.isEmailAvailable(email);
        GetEmailCheckResponse response;
        if (isEmailAvailable) {
            response = new GetEmailCheckResponse(true, "새로운 이메일입니다.");
        } else {
            response = new GetEmailCheckResponse(false, "중복된 이메일입니다.");
        }
        return new BaseResponse<>(response);
    }


    @PostMapping("/signup")
    public BaseResponse<PostUserResponse> signUp(@Validated @RequestBody PostUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        log.info("User sign-up request: {}", request.getEmail());
        User user = userService.createUser(request);
        return new BaseResponse<>(new PostUserResponse(user.getId()));
    }

    @PatchMapping("/{id}/status")
    public BaseResponse<Void> updateUserStatus(@PathVariable Long id, @RequestParam Status status) {
        log.info("Update user status request for ID: {}, Status: {}", id, status);
        userService.updateUserStatus(id, status);
        return new BaseResponse<>(null);
    }

    @PostMapping("/login")
    public BaseResponse<PostLoginResponse> login(@Validated @RequestBody PostLoginRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        log.info("User login request: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        JwtTokenResponse tokenResponse = userService.login(request);
        return new BaseResponse<>(new PostLoginResponse(user.getId(), tokenResponse));
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestHeader("Authorization") String tokenRequest) {
        log.info("User logout request");
        String token = tokenRequest.split(" ")[1];
        userService.logout(token);
        return new BaseResponse<>(null);
    }

    @GetMapping("/{id}")
    public BaseResponse<GetUserResponse> getUserInfo(@PathVariable Long id) {
        log.info("Get user info for ID: {}", id);
        User user = userService.getUser(id);
        GetUserResponse response = UserDtoConverter.userToGetUserResponse(user);
        return new BaseResponse<>(response);
    }

    @PatchMapping("/profile")
    public BaseResponse<Void> modifyUser(Authentication authentication, @RequestBody PostModifyRequest modifyRequest) {
        log.info("Modify user profile request for current user");
        userService.modifyUser(authentication, modifyRequest);
        return new BaseResponse<>(null);
    }


    @GetMapping("/{id}/tours")
    public BaseResponse<List<GetTourResponse>> getUserTours(Authentication authentication, @PathVariable Long id, @RequestParam(required = false) String option) {
        log.info("Get tours for user ID: {}, Option: {}", id, option);
        List<GetTourResponse> responses = userService.getUserTours(id, option, authentication);
        return new BaseResponse<>(responses);
    }

    @PostMapping("/wishlist/{tourId}")
    public BaseResponse<Void> toggleWishlist(Authentication authentication, @PathVariable Long tourId) {
        log.info("Toggle wishlist for tour ID: {}", tourId);
        userService.toggleWishlist(authentication, tourId);
        return new BaseResponse<>(null);
    }

    @GetMapping("/wishlist")
    public BaseResponse<List<GetTourResponse>> getWishlist(Authentication authentication) {
        log.info("Get wishlist for current user");
        List<GetTourResponse> responses = userService.getUserWishlist(authentication);
        return new BaseResponse<>(responses);
    }

    @GetMapping("/{id}/chats")
    public BaseResponse<GetUserChatsResponse> getUserChats(@PathVariable Long id, @RequestParam String option) {
        log.info("Get chats for user ID: {}, Option: {}", id, option); // option값: traveler, leader
        // TODO: 유저 채팅 조회 로직 구현 예정
        return new BaseResponse<>(new GetUserChatsResponse());
    }

    @PostMapping("/block")
    public BaseResponse<Void> blockUser(@RequestParam Long userId, @RequestParam Long targetId) {
        log.info("Block user request: User {} blocks User {}", userId, targetId);
        userService.blockUser(userId, targetId);
        return new BaseResponse<>(null);
    }

}
