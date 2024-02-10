package peoplehere.peoplehere.controller.dto.user;

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
                user.getName(),
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
                user.getStatus()
        );
    }

    public static User postUserRequestToUser(PostUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .gender(request.getGender())
                .imageUrl(null)
                .content(request.getContent())
                .birth(request.getBirth())
                .address(request.getAddress())
                .job(request.getJob())
                .almaMater(request.getAlmaMater())
                .hobby(request.getHobby())
                .pet(request.getPet())
                .favourite(request.getFavourite())
                .build();
    }
}
