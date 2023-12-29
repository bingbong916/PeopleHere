package peoplehere.peoplehere.controller.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import peoplehere.peoplehere.controller.dto.message.GetMessageResponse;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetChatResponse {
    private Long chatId;
    private Long tourId;
    private List<GetMessageResponse> messages;
}
