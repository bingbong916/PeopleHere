package peoplehere.peoplehere.service.user;

import java.time.LocalDate;
import java.time.Period;
import lombok.RequiredArgsConstructor;
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
import peoplehere.peoplehere.domain.JwtBlackList;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.domain.enums.LoginType;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.repository.*;
import peoplehere.peoplehere.util.jwt.JwtProvider;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;
import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.DUPLICATE_PHONE_NUMBER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtBlackListRepository jwtBlackListRepository;
    protected final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

<<<<<<< HEAD
    public AuthService(UserRepository userRepository, WishlistRepository wishlistRepository, SearchHistoryRepository searchHistoryRepository, TourRepository tourRepository, UserBlockRepository userBlockRepository, UserLanguageRepository userLanguageRepository, PasswordEncoder passwordEncoder, JwtBlackListRepository jwtBlackListRepository, S3Service s3Service, LanguageRepository languageRepository, UserQuestionRepository userQuestionRepository, QuestionRepository questionRepository, JwtProvider jwtProvider) {
        super(userRepository, wishlistRepository, searchHistoryRepository, tourRepository, userBlockRepository, userLanguageRepository, passwordEncoder, jwtBlackListRepository, s3Service, languageRepository, userQuestionRepository, questionRepository);
        this.jwtProvider = jwtProvider;
    }

=======
>>>>>>> upstream/master

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


    protected void validatePassword(String password, String encodedPassword) {
        log.info("password: " + password);
        log.info("encodedPassword: " + encodedPassword);
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new UserException(PASSWORD_NO_MATCH);
        }
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

    /**
     * 로그아웃
     */
    public void logout(String token) {
        JwtBlackList blackList = jwtBlackListRepository.findByToken(token);
        if (blackList == null) {
            jwtBlackListRepository.save(new JwtBlackList(token));
        }
    }

    public boolean isPhoneNumberAvailable(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isEmpty();
    }

    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }


}
