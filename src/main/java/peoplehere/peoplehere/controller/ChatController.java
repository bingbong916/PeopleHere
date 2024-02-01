package peoplehere.peoplehere.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import peoplehere.peoplehere.common.exception.ChatException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.chat.*;
import peoplehere.peoplehere.service.MessageService;
import peoplehere.peoplehere.util.BindingResultUtils;
import peoplehere.peoplehere.service.ChatService;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    @PostMapping("/new")
    public BaseResponse<GetChatResponse> createChat(@Valid @RequestBody PostChatRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = BindingResultUtils.getErrorMessages(bindingResult);
            throw new ChatException(INVALID_CHAT_VALUE, errorMessages);
        }
        log.info("Create chat request: {}", request.getTourId());
        Long chatRoomId = chatService.createChatRoom(request);
        // TODO: 채팅방 생성 로직 구현 예정
        return new BaseResponse(chatRoomId);
    }

    @PatchMapping("/{chatId}")
    public BaseResponse<Void> deleteChat(@PathVariable Long chatId) {
        log.info("Delete chat request for ID: {}", chatId);
        // TODO: 채팅방 삭제 로직 구현 예정
        return new BaseResponse<>(null);
    }

    @GetMapping("/{chatId}")
    public BaseResponse<GetChatResponse> getChat(@PathVariable Long chatId) {
        log.info("Get chat request for ID: {}", chatId);
        // TODO: 특정 채팅방 조회 로직 구현 예정
        throw new ChatException(CHAT_NOT_FOUND, "Chat ID: " + chatId + " not found");
    }
}
