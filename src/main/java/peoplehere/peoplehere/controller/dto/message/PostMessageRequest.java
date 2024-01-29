package peoplehere.peoplehere.controller.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 사용자가 메시지를 보낼 때 사용하는 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostMessageRequest {

    private Long chatId; // 방 번호
    private Long userId; // 채팅을 보낸 사람

    @NotBlank(message = "메시지 내용은 필수입니다.")
    @Size(max = 1000, message = "메시지 내용은 최대 1000자까지 가능합니다.")
    private String message; // 메시지
}