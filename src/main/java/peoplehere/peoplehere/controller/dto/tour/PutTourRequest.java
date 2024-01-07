package peoplehere.peoplehere.controller.dto.tour;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PutTourRequest {
    @NotNull(message = "투어 ID는 필수입니다.")
    private Long tourId;

    @NotBlank(message = "투어 이름은 필수입니다.")
    @Size(max = 100, message = "투어 이름은 최대 100자까지 가능합니다.")
    private String name;

    @Min(value = 0, message = "예산은 0 이상이어야 합니다.")
    private int budget;

    private Date startDate;

    @Min(value = 0, message = "시간은 0 이상이어야 합니다.")
    private int time;

    private String imageUrl;

    private String content;
}
