package peoplehere.peoplehere.service;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.controller.dto.message.PostMessageRequest;
import peoplehere.peoplehere.domain.Chat;
import peoplehere.peoplehere.domain.Message;
import peoplehere.peoplehere.repository.ChatRepository;
import peoplehere.peoplehere.repository.MessageRepository;
import peoplehere.peoplehere.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveMessage(PostMessageRequest postMessageRequest) {
        Chat chat = chatRepository.findById(postMessageRequest.getChatId())
            .orElseThrow(() -> new NoSuchElementException(
                "ChatRoom not found with id: " + postMessageRequest.getChatId()));

        Message message = new Message(
            userRepository.findById(postMessageRequest.getUserId()).orElseThrow(),
            chat, postMessageRequest.getMessage());
        messageRepository.save(message);
    }

    public List<Message> getMessages(Long chatId){
        return messageRepository.findByChatId(chatId);
    }

}
