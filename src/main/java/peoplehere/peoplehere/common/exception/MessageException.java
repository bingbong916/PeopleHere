package peoplehere.peoplehere.common.exception;

import lombok.Getter;
import peoplehere.peoplehere.common.response.status.ResponseStatus;

@Getter
public class MessageException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public MessageException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
    public MessageException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
