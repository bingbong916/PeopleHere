package peoplehere.peoplehere.controller.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostChatRequest {
    private List<Long> userIds;
    private Long tourId;
}
