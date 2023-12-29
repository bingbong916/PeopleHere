package peoplehere.peoplehere.controller.dto.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class PutReviewRequest {
    @NotBlank(message = "리뷰 내용은 필수입니다.")
    @Size(max = 500, message = "리뷰 내용은 최대 500자까지 가능합니다.")
    private String content;

    @Min(value = 0, message = "점수는 0 이상이어야 합니다.")
    @Max(value = 5, message = "점수는 5 이하이어야 합니다.")
    private float score;
}
