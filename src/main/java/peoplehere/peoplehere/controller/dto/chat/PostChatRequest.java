package peoplehere.peoplehere.controller.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostChatRequest {
    @NotEmpty(message = "사용자 ID 목록은 비어 있으면 안됩니다.")
    private List<@NotNull(message = "사용자 ID는 null이면 안됩니다.") Long> userIds;

    @NotNull(message = "투어 ID는 필수입니다.")
    private Long tourId;
}
