package peoplehere.peoplehere.controller.dto.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import peoplehere.peoplehere.controller.dto.auth.PostEmailUserRequest;
import peoplehere.peoplehere.controller.dto.auth.PostPhoneNumberUserRequest;
import peoplehere.peoplehere.domain.User;
import peoplehere.peoplehere.domain.UserQuestion;

public class UserDtoConverter {

    /**
     * User -> GetUserResponse
     * 유저 -> 유저 조회 DTO
     */

    public static GetUserResponse userToGetUserResponse(User user) {
        List<String> languages = user.getLanguages().stream()
                .map(userLanguage -> userLanguage.getLanguage().getKoreanName())
                .toList();

        Map<String, String> questions = user.getUserQuestions().stream()
                .collect(Collectors.toMap(
                        uq -> uq.getQuestion().getQuestion(),
                        UserQuestion::getAnswer
                ));
        return new GetUserResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getGender(),
            user.getImageUrl(),
            user.getContent(),
            user.getAddress(),
            user.getBirth(),
            user.getJob(),
            user.getAlmaMater(),
            user.getHobby(),
            user.getPet(),
            user.getFavourite(),
            user.getStatus(),
            languages,
            questions
        );
    }

    public static User postEmailUserRequestToUser(PostEmailUserRequest request) {
        return User.builder()
            .email(request.getEmail())
            .password(request.getPassword())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .birth(request.getBirth())
            .gender(request.getGender())
            .marketingConsent(request.getMarketingConsent())
            .build();
    }

    public static User postPhoneNumberRequestToUser(PostPhoneNumberUserRequest request) {
        return User.builder()
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .password(request.getPassword())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .birth(request.getBirth())
            .gender(request.getGender())
            .marketingConsent(request.getMarketingConsent())
            .build();
    }
}
