package peoplehere.peoplehere.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.controller.dto.chat.GetChatResponse;
import peoplehere.peoplehere.controller.dto.chat.PostChatRequest;
import peoplehere.peoplehere.controller.dto.message.GetMessageResponse;
import peoplehere.peoplehere.domain.Chat;
import peoplehere.peoplehere.domain.Message;
import peoplehere.peoplehere.repository.ChatRepository;
import peoplehere.peoplehere.repository.TourRepository;
import peoplehere.peoplehere.repository.UserRepository;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;

    @Transactional
    public Long createChatRoom(PostChatRequest postChatRequest) {
        Chat result = chatRepository.save(new Chat(tourRepository.findById(
            postChatRequest.getTourId()).orElseThrow(),
            userRepository.findById(postChatRequest.getUserId()).orElseThrow()));
        return result.getId();
    }

    public List<GetChatResponse> getChatRoom(Long tourId) {
        List<Chat> chatList = chatRepository.findByTourId(tourId);
        List<GetChatResponse> getChatResponses = new ArrayList<>();
        for (Chat chat : chatList) {
            List<Message> messages = chat.getMessages();
            List<GetMessageResponse> messageResponses = new ArrayList<>();
            for (Message message : messages) {
                messageResponses.add(
                    new GetMessageResponse(message.getId(), message.getChat().getId(),
                        message.getUser().getId(), message.getContent()));
            }
            getChatResponses.add(new GetChatResponse(chat.getId(), chat.getUser().getId(), chat.getTour().getId(),
                messageResponses));
        }
        return getChatResponses;
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        chatRepository.deleteById(chatRoomId);
    }
}
