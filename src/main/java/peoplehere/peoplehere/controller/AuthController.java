package peoplehere.peoplehere.controller;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.INVALID_USER_VALUE;
import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;
import static peoplehere.peoplehere.util.BindingResultUtils.getErrorMessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.auth.GetPhoneNumberCheckResponse;
import peoplehere.peoplehere.controller.dto.jwt.JwtTokenResponse;
import peoplehere.peoplehere.controller.dto.auth.GetEmailCheckResponse;
import peoplehere.peoplehere.controller.dto.auth.PostEmailLoginRequest;
import peoplehere.peoplehere.controller.dto.auth.PostEmailUserRequest;
import peoplehere.peoplehere.controller.dto.auth.PostLoginResponse;
import peoplehere.peoplehere.controller.dto.auth.PostPhoneNumberLoginRequest;
import peoplehere.peoplehere.controller.dto.auth.PostPhoneNumberUserRequest;
import peoplehere.peoplehere.controller.dto.user.PostUserResponse;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.repository.UserRepository;
import peoplehere.peoplehere.service.user.EmailUserService;
import peoplehere.peoplehere.service.user.PhoneNumberUserService;
import peoplehere.peoplehere.service.user.UserService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PhoneNumberUserService phoneNumberUserService;
    private final EmailUserService emailUserService;
    private final UserRepository userRepository;

    /**
     * 이메일 중복 체크
     */
    @GetMapping("/check-email")
    public BaseResponse<GetEmailCheckResponse> checkEmail(@RequestParam String email) {
        boolean isEmailAvailable = emailUserService.isEmailAvailable(email);
        GetEmailCheckResponse response;
        if (isEmailAvailable) {
            response = new GetEmailCheckResponse(true, "새로운 이메일입니다.");
        } else {
            response = new GetEmailCheckResponse(false, "중복된 이메일입니다.");
        }
        return new BaseResponse<>(response);
    }

    /**
     * 핸드폰 번호 중복 체크
     */
    @GetMapping("/check-phone")
    public BaseResponse<GetPhoneNumberCheckResponse> checkPhone(@RequestParam String phoneNumber) {
        boolean isPhoneNumberAvailable = phoneNumberUserService.isPhoneNumberAvailable(phoneNumber);
        GetPhoneNumberCheckResponse response;
        if (isPhoneNumberAvailable) {
            response = new GetPhoneNumberCheckResponse(true, "새로운 핸드폰 번호입니다.");
        } else {
            response = new GetPhoneNumberCheckResponse(false, "중복된 핸드폰 번호입니다.");
        }
        return new BaseResponse<>(response);
    }


    /**
     * 이메일로 회원가입
     */
    @PostMapping("/signup-email")
    public BaseResponse<PostUserResponse> signUpWithEmail(@Validated @RequestBody PostEmailUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        log.info("Email User sign-up request: {}", request.getEmail());
        User user = emailUserService.createUser(request);
        return new BaseResponse<>(new PostUserResponse(user.getId()));
    }

    /**
     * 핸드폰 번호로 회원가입
     */
    @PostMapping("/signup-phone")
    public BaseResponse<PostUserResponse> signUpWithPhone(@Validated @RequestBody PostPhoneNumberUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        log.info("PhoneNumber User sign-up request: {}", request.getEmail());
        User user = phoneNumberUserService.createUser(request);
        return new BaseResponse<>(new PostUserResponse(user.getId()));
    }

    /**
     * 이메일로 로그인
     */
    @PostMapping("/login-email")
    public BaseResponse<PostLoginResponse> login(@Validated @RequestBody PostEmailLoginRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        log.info("Email User login request: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        JwtTokenResponse tokenResponse = emailUserService.login(request);
        return new BaseResponse<>(new PostLoginResponse(user.getId(), tokenResponse));
    }

    /**
     * 핸드폰 번호로 로그인
     */
    @PostMapping("/login-phone")
    public BaseResponse<PostLoginResponse> login(@Validated @RequestBody PostPhoneNumberLoginRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserException(INVALID_USER_VALUE, getErrorMessages(bindingResult));
        }
        log.info("PhoneNumber User login request: {}", request.getPhoneNumber());
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        JwtTokenResponse tokenResponse = phoneNumberUserService.login(request);
        return new BaseResponse<>(new PostLoginResponse(user.getId(), tokenResponse));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestHeader("Authorization") String tokenRequest) {
        log.info("User logout request");
        String token = tokenRequest.split(" ")[1];
        userService.logout(token);
        return new BaseResponse<>(null);
    }


}
