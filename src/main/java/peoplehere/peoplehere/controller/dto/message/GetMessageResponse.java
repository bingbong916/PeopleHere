package peoplehere.peoplehere.controller.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetMessageResponse {
    private Long messageId;
    private Long chatId;
    private Long userId;
    private String content;
}
