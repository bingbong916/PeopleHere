package peoplehere.peoplehere.controller.dto.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostMessageRequest {
    private Long chatId;
    private Long userId;
    private String content;
}
