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
import peoplehere.peoplehere.domain.enums.LoginType;
import peoplehere.peoplehere.service.S3Service;
import peoplehere.peoplehere.controller.dto.jwt.JwtTokenResponse;
import peoplehere.peoplehere.controller.dto.user.*;
import peoplehere.peoplehere.domain.*;
import peoplehere.peoplehere.domain.enums.Status;
import peoplehere.peoplehere.repository.*;
import peoplehere.peoplehere.util.jwt.JwtProvider;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@Transactional
public class EmailUserService extends UserService {

    private final JwtProvider jwtProvider;

    public EmailUserService(UserRepository userRepository,
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

    /**
     * 회원 가입
     */
    public User createUser(PostEmailUserRequest postUserRequest) {
        log.info("[UserService.createUser]");

        // 이메일 중복 검사
        validateEmail(postUserRequest.getEmail());

        // 만 18세 이상 검사
        LocalDate today = LocalDate.now();
        LocalDate birthDate = postUserRequest.getBirth();
        int age = Period.between(birthDate, today).getYears();

        if (age < 18) {
            throw new UserException(USER_NOT_ADULT);
        }

        //패스워드 암호화
        String encodedPassword = passwordEncoder.encode(postUserRequest.getPassword());
        postUserRequest.resetPassword(encodedPassword);

        //DB 저장
        User user = UserDtoConverter.postEmailUserRequestToUser(postUserRequest);
        user.setLoginType(LoginType.EMAIL);
        userRepository.save(user);

        return user;
    }

    private void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException(DUPLICATE_EMAIL);
        }
    }

    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }


    /**
     * 로그인
     */
    public JwtTokenResponse login(PostEmailLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));
        if (user.getStatus() == Status.DELETED) {
            throw new UserException(USER_DELETED);
        }
        validatePassword(request.getPassword(), user.getPassword());
        String accessToken = jwtProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());
        return new JwtTokenResponse(accessToken, refreshToken);
    }


}
