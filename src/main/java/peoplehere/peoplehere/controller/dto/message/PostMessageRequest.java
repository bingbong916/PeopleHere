package peoplehere.peoplehere.controller.dto.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class PostMessageRequest {
    private Long chatId;
    private Long userId;
    private String userName;

    @NotBlank(message = "메시지 내용은 필수입니다.")
    @Size(max = 1000, message = "메시지 내용은 최대 1000자까지 가능합니다.")
    private String content;
}
