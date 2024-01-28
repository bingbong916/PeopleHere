package peoplehere.peoplehere.controller.dto.tour;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import peoplehere.peoplehere.controller.dto.place.PlaceInfoDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostTourRequest {
    private Long userId;

    @NotBlank(message = "투어 이름은 필수입니다.")
    @Size(max = 100, message = "투어 이름은 최대 100자까지 가능합니다.")
    private String name;

    @Min(value = 0, message = "시간은 0 이상이어야 합니다.")
    private int time;

    private String imageUrl;

    @Size(max = 500, message = "소개글은 최대 500자까지 가능합니다.")
    private String content;

    //TODO: 예외처리 필요
    private List<String> categoryNames;

    // TODO: notice 필드 추가 필요.

    private List<PlaceInfoDto> places;
}
