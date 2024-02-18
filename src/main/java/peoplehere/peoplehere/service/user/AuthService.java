package peoplehere.peoplehere.service.user;

import java.time.LocalDate;
import java.time.Period;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.controller.dto.auth.PostEmailLoginRequest;
import peoplehere.peoplehere.controller.dto.auth.PostEmailUserRequest;
import peoplehere.peoplehere.controller.dto.auth.PostPhoneNumberLoginRequest;
import peoplehere.peoplehere.controller.dto.auth.PostPhoneNumberUserRequest;
import peoplehere.peoplehere.controller.dto.jwt.JwtTokenResponse;
import peoplehere.peoplehere.controller.dto.user.UserDtoConverter;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.domain.enums.LoginType;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.repository.*;
import peoplehere.peoplehere.service.S3Service;
import peoplehere.peoplehere.util.jwt.JwtProvider;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;
import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.DUPLICATE_PHONE_NUMBER;

@Slf4j
@Service
@Transactional
public class AuthService extends UserService {

    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, WishlistRepository wishlistRepository, TourRepository tourRepository, UserBlockRepository userBlockRepository, UserLanguageRepository userLanguageRepository, PasswordEncoder passwordEncoder, JwtBlackListRepository jwtBlackListRepository, S3Service s3Service, LanguageRepository languageRepository, UserQuestionRepository userQuestionRepository, QuestionRepository questionRepository, JwtProvider jwtProvider) {
        super(userRepository, wishlistRepository, tourRepository, userBlockRepository, userLanguageRepository, passwordEncoder, jwtBlackListRepository, s3Service, languageRepository, userQuestionRepository, questionRepository);
        this.jwtProvider = jwtProvider;
    }


    public User createUser(Object request) {
        User user;
        String encodedPassword;

        if (request instanceof PostEmailUserRequest emailRequest) {
            encodedPassword = passwordEncoder.encode(emailRequest.getPassword());
            user = UserDtoConverter.postEmailUserRequestToUser(emailRequest, encodedPassword);
            user.setLoginType(LoginType.EMAIL);
            validateEmail(emailRequest.getEmail());
            validateAge(emailRequest.getBirth());
        } else if (request instanceof PostPhoneNumberUserRequest phoneRequest) {
            encodedPassword = passwordEncoder.encode(phoneRequest.getPassword());
            user = UserDtoConverter.postPhoneNumberRequestToUser(phoneRequest, encodedPassword);
            user.setLoginType(LoginType.PHONE_NUMBER);
            validatePhoneNumber(phoneRequest.getPhoneNumber());
            validateEmail(phoneRequest.getEmail());
            validateAge(phoneRequest.getBirth());
        } else {
            throw new UserException(BAD_REQUEST);
        }

        userRepository.save(user);
        return user;
    }

    private void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException(DUPLICATE_EMAIL);
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new UserException(DUPLICATE_PHONE_NUMBER);
        }
    }

    private void validateAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        int age = Period.between(birthDate, today).getYears();
        if (age < 18) {
            throw new UserException(USER_NOT_ADULT);
        }
    }


    /**
     * 로그인
     */
    public JwtTokenResponse login(Object request) {
        User user;
        String identifier;
        String password;

        if (request instanceof PostEmailLoginRequest emailLoginRequest) {
            user = userRepository.findByEmail(emailLoginRequest.getEmail())
                    .orElseThrow(() -> new UserException(USER_NOT_FOUND));
            password = emailLoginRequest.getPassword();
            identifier = emailLoginRequest.getEmail();
        } else if (request instanceof PostPhoneNumberLoginRequest phoneLoginRequest) {
            user = userRepository.findByPhoneNumber(phoneLoginRequest.getPhoneNumber())
                    .orElseThrow(() -> new UserException(USER_NOT_FOUND));
            password = phoneLoginRequest.getPassword();
            identifier = phoneLoginRequest.getPhoneNumber();
        } else {
            throw new UserException(BAD_REQUEST);
        }
        if (user.getStatus() == Status.DELETED) {
            throw new UserException(USER_DELETED);
        }
        validatePassword(password, user.getPassword());
        String accessToken = jwtProvider.createAccessToken(identifier);
        String refreshToken = jwtProvider.createRefreshToken(identifier);
        return new JwtTokenResponse(accessToken, refreshToken);
    }

    public boolean isPhoneNumberAvailable(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isEmpty();
    }
    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }



}
