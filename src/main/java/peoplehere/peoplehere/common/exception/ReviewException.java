package peoplehere.peoplehere.common.exception;

import lombok.Getter;
import peoplehere.peoplehere.common.response.status.ResponseStatus;

@Getter
public class ReviewException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public ReviewException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public ReviewException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
