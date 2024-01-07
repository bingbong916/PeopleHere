package peoplehere.peoplehere.controller.dto.user;

import peoplehere.peoplehere.domain.User;

public class UserDtoConverter {

    /**
     * User -> GetUserResponse
     * 유저 -> 유저 조회 DTO
     */
    public static GetUserResponse userToGetUserResponse(User user) {
        GetUserResponse getUserResponse = new GetUserResponse();
        getUserResponse.setEmail(user.getEmail());
        getUserResponse.setName(user.getName());
        getUserResponse.setGender(user.getGender());
        getUserResponse.setLeader(user.isLeader());
        getUserResponse.setImageUrl(user.getImageUrl());
        getUserResponse.setContent(user.getContent());
        return getUserResponse;
    }

    public static User postUserRequestToUser(PostUserRequest postUserRequest) {

       return new User(postUserRequest.getEmail(),postUserRequest.getPassword(),postUserRequest.getName(),
                postUserRequest.getGender(),false,postUserRequest.getImageUrl(),postUserRequest.getContent());

    }


}
