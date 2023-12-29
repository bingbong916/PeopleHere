package peoplehere.peoplehere.common.exception;

import lombok.Getter;
import peoplehere.peoplehere.common.response.status.ResponseStatus;

@Getter
public class ChatException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public ChatException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
    public ChatException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
