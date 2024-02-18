package peoplehere.peoplehere.controller.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import peoplehere.peoplehere.controller.dto.user.UserInfoDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewResponse {
    private Long reviewId;
    private UserInfoDto reviewer;
    private String content;
    private LocalDateTime createdAt;
}