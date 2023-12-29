package peoplehere.peoplehere.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.common.exception.MessageException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.message.*;
import peoplehere.peoplehere.service.MessageService;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/new")
    public BaseResponse<GetMessageResponse> createMessage(@RequestBody PostMessageRequest request) {
        log.info("Create message request: {}", request.getContent());
        // TODO: 메시지 생성 로직 구현 예정
        return new BaseResponse<>(new GetMessageResponse(1L, request.getChatId(), request.getUserId(), request.getContent()));
    }

    @PatchMapping("/{messageId}")
    public BaseResponse<Void> deleteMessage(@PathVariable Long messageId) {
        log.info("Delete message request for ID: {}", messageId);
        // TODO: 메시지 삭제 로직 구현 예정
        return new BaseResponse<>(null);
    }

    @GetMapping("/{messageId}")
    public BaseResponse<GetMessageResponse> getMessage(@PathVariable Long messageId) {
        log.info("Get message request for ID: {}", messageId);
        // TODO: 특정 메시지 조회 로직 구현 예정
        throw new MessageException(MESSAGE_NOT_FOUND, "Message ID: " + messageId + " not found");
    }
}
