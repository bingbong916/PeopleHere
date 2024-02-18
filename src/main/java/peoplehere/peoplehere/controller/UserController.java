package peoplehere.peoplehere.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.tour.GetTourResponse;
import peoplehere.peoplehere.controller.dto.user.*;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.service.user.UserService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/password")
    public BaseResponse<Void> updatePassword(Authentication authentication, @RequestBody String newPassword) {
        userService.updatePassword(authentication, newPassword);
        return new BaseResponse<>(null);
    }

    @PatchMapping("/{id}/status")
    public BaseResponse<Void> updateUserStatus(@PathVariable Long id, @RequestParam Status status) {
        log.info("Update user status request for ID: {}, Status: {}", id, status);
        userService.updateUserStatus(id, status);
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

    @PostMapping("/search-history")
    public BaseResponse<Void> addSearchHistory(Authentication authentication, PostSearchHistoryRequest request) {
        log.info("Add search history for current user");
        userService.addSearchHistory(authentication, request);
        return new BaseResponse<>(null);
    }

    @GetMapping("/search-histories")
    public BaseResponse<List<GetSearchHistoryResponse>> getSearchHistories(Authentication authentication) {
        log.info("Get search histories for current user");
        List<GetSearchHistoryResponse> searchHistories = userService.getSearchHistories(authentication);
        return new BaseResponse<>(searchHistories);
    }

    @GetMapping("/user/onboarding")
    public BaseResponse<GetOnboardingStatusResponse> getOnboardingStatus(Authentication authentication) {
        GetOnboardingStatusResponse response = userService.getOnboardingStatus(authentication);
        return new BaseResponse<>(response);
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
