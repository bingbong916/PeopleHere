package peoplehere.peoplehere.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import peoplehere.peoplehere.common.exception.ChatException;
import peoplehere.peoplehere.common.response.BaseResponse;
import peoplehere.peoplehere.controller.dto.chat.*;
import peoplehere.peoplehere.util.BindingResultUtils;
import peoplehere.peoplehere.service.ChatService;

import static peoplehere.peoplehere.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/new")
    public BaseResponse<GetChatResponse> createChat(@Valid @RequestBody PostChatRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = BindingResultUtils.getErrorMessages(bindingResult);
            throw new ChatException(INVALID_CHAT_VALUE, errorMessages);
        }
        log.info("Create chat request: {}", request.getTourId());
        Long chatRoomId = chatService.createChatRoom(request);
        return new BaseResponse(chatRoomId);
    }

    @PatchMapping("/{chatId}")
    public BaseResponse<Void> deleteChat(@PathVariable Long chatId) {
        log.info("Delete chat request for ID: {}", chatId);
        chatService.deleteChatRoom(chatId);
        return new BaseResponse(chatId);
    }

    @GetMapping("/{chatId}")
    public BaseResponse<GetChatResponse> getChat(@PathVariable Long chatId) {
        log.info("Get chat request for ID: {}", chatId);
        GetChatResponse chatResponse = chatService.getChat(chatId);
        return new BaseResponse<>(chatResponse);
    }

    @GetMapping("/tours/{tourId}")
    public BaseResponse<List<GetChatResponse>> getChatsByTourId(@PathVariable Long tourId) {
        log.info("Get chat request for tourID: {}", tourId);
        List<GetChatResponse> chats = chatService.getChatsByTourId(tourId);
        return new BaseResponse<>(chats);
    }
}
