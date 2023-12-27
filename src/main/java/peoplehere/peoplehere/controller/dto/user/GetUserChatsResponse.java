package peoplehere.peoplehere.controller.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetUserChatsResponse {
    private List<ChatInfo> chats;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ChatInfo {
        private Long id;
        private Long tourId;
        private List<MessageInfo> messages;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class MessageInfo {
            private Long id;
            private String content;
            private String createdAt;
        }
    }
}

