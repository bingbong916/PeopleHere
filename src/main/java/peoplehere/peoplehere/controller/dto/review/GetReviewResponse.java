package peoplehere.peoplehere.controller.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewResponse {
    private Long reviewId;
    private Long userId;
    private Long tourId;
    private String content;
    private float score;
}
