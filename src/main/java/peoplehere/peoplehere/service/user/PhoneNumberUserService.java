package peoplehere.peoplehere.service.user;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.DUPLICATE_EMAIL;
import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.USER_DELETED;
import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.USER_NOT_ADULT;
import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;

import java.time.LocalDate;
import java.time.Period;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.common.exception.UserException;
import peoplehere.peoplehere.controller.dto.jwt.JwtTokenResponse;
import peoplehere.peoplehere.controller.dto.auth.PostPhoneNumberLoginRequest;
import peoplehere.peoplehere.controller.dto.auth.PostPhoneNumberUserRequest;
import peoplehere.peoplehere.controller.dto.user.UserDtoConverter;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.domain.enums.LoginType;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.repository.JwtBlackListRepository;
import peoplehere.peoplehere.repository.LanguageRepository;
import peoplehere.peoplehere.repository.QuestionRepository;
import peoplehere.peoplehere.repository.TourRepository;
import peoplehere.peoplehere.repository.UserBlockRepository;
import peoplehere.peoplehere.repository.UserLanguageRepository;
import peoplehere.peoplehere.repository.UserQuestionRepository;
import peoplehere.peoplehere.repository.UserRepository;
import peoplehere.peoplehere.repository.WishlistRepository;
import peoplehere.peoplehere.service.S3Service;
import peoplehere.peoplehere.util.jwt.JwtProvider;

@Slf4j
@Service
@Transactional
public class PhoneNumberUserService extends UserService {

    private final JwtProvider jwtProvider;

    public PhoneNumberUserService(UserRepository userRepository,
        WishlistRepository wishlistRepository,
        TourRepository tourRepository,
        UserBlockRepository userBlockRepository,
        UserLanguageRepository userLanguageRepository,
        PasswordEncoder passwordEncoder,
        JwtBlackListRepository jwtBlackListRepository,
        S3Service s3Service, LanguageRepository languageRepository,
        UserQuestionRepository userQuestionRepository,
        QuestionRepository questionRepository, JwtProvider jwtProvider) {
        super(userRepository, wishlistRepository, tourRepository, userBlockRepository,
            userLanguageRepository, passwordEncoder, jwtBlackListRepository, s3Service,
            languageRepository, userQuestionRepository, questionRepository);
        this.jwtProvider = jwtProvider;
    }

    public User createUser(PostPhoneNumberUserRequest request) {
        log.info("[UserService.createUser]");

        // 핸드폰 번호 중복 검사
        validatePhoneNumber(request.getEmail());

        // 만 18세 이상 검사
        LocalDate today = LocalDate.now();
        LocalDate birthDate = request.getBirth();
        int age = Period.between(birthDate, today).getYears();

        if (age < 18) {
            throw new UserException(USER_NOT_ADULT);
        }

        //패스워드 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.resetPassword(encodedPassword);

        //DB 저장
        User user = UserDtoConverter.postPhoneNumberRequestToUser(request);
        user.setLoginType(LoginType.PHONE_NUMBER);
        userRepository.save(user);

        return user;
    }

    private void validatePhoneNumber(String email) {
        if (userRepository.findByPhoneNumber(email).isPresent()) {
            throw new UserException(DUPLICATE_EMAIL);
        }
    }

    public boolean isPhoneNumberAvailable(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isEmpty();
    }


    /**
     * 로그인
     */
    public JwtTokenResponse login(PostPhoneNumberLoginRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        if (user.getStatus() == Status.DELETED) {
            throw new UserException(USER_DELETED);
        }
        validatePassword(request.getPassword(), user.getPassword());
        String accessToken = jwtProvider.createAccessToken(user.getPhoneNumber());
        String refreshToken = jwtProvider.createRefreshToken(user.getPhoneNumber());
        return new JwtTokenResponse(accessToken, refreshToken);
    }

}
