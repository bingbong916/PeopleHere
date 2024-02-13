package peoplehere.peoplehere.controller.dto.user;

import java.util.ArrayList;
import peoplehere.peoplehere.domain.User;

public class UserDtoConverter {

    /**
     * User -> GetUserResponse
     * 유저 -> 유저 조회 DTO
     */

    public static GetUserResponse userToGetUserResponse(User user) {
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
            new ArrayList<>()
        );
    }

    public static User postUserRequestToUser(PostUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .build();
    }
}
