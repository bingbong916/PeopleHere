package peoplehere.peoplehere.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.controller.dto.message.*;
import peoplehere.peoplehere.service.MessageService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload PostMessageRequest message) {
        log.info("Get chat request");
        // 채팅 저장
        messageService.saveMessage(message);
        // 해당 채팅 메시지를 WebSocket 토픽(/topic/채팅방ID)에 전송하여 클라이언트에게 브로드캐스팅한다.
        messagingTemplate.convertAndSend("/topic/" + message.getChatId(), message);
    }

//    @PostMapping("/new")
//    public BaseResponse<GetMessageResponse> createMessage(@Valid @RequestBody PostMessageRequest request, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            String errorMessages = BindingResultUtils.getErrorMessages(bindingResult);
//            throw new MessageException(INVALID_MESSAGE_VALUE, errorMessages);
//        }
//        log.info("Create message request: {}", request.getMessage());
//        //메시지 생성 로직 구현 예정
//        return new BaseResponse<>(new GetMessageResponse(1L, request.getChatId(), request.getUserId(), request.getMessage()));
//    }
//
//    @PatchMapping("/{messageId}")
//    public BaseResponse<Void> deleteMessage(@PathVariable Long messageId) {
//        log.info("Delete message request for ID: {}", messageId);
//        //메시지 삭제 로직 구현 예정
//        return new BaseResponse<>(null);
//    }
//
//    @GetMapping("/{messageId}")
//    public BaseResponse<GetMessageResponse> getMessage(@PathVariable Long messageId) {
//        log.info("Get message request for ID: {}", messageId);
//        // 특정 메시지 조회 로직 구현 예정
//        throw new MessageException(MESSAGE_NOT_FOUND, "Message ID: " + messageId + " not found");
//    }
}
