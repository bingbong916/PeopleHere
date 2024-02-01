package peoplehere.peoplehere.controller.dto.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 새로운 채팅을 만들 때 요청 하는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class PostChatRequest {
    @NotNull
    private Long userId;

    @NotNull(message = "투어 ID는 필수입니다.")
    private Long tourId;
}
